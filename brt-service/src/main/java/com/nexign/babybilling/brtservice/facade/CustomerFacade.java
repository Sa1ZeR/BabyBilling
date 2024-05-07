package com.nexign.babybilling.brtservice.facade;

import com.nexign.babybilling.CustomerRole;
import com.nexign.babybilling.brtservice.entity.Customer;
import com.nexign.babybilling.brtservice.entity.CustomerCall;
import com.nexign.babybilling.brtservice.entity.Tariff;
import com.nexign.babybilling.brtservice.repo.CustomerCallsRepo;
import com.nexign.babybilling.brtservice.service.*;
import com.nexign.babybilling.brtservice.service.cache.CustomerCache;
import com.nexign.babybilling.domain.Pair;
import com.nexign.babybilling.payload.events.CdrCalcedEvent;
import com.nexign.babybilling.payload.events.ChangeTariffEvent;
import com.nexign.babybilling.payload.events.CreateNewCustomerEvent;
import com.nexign.babybilling.payload.events.CustomerPaymentEvent;
import com.nexign.babybilling.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerFacade {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(Math.min(6, Runtime.getRuntime().availableProcessors()));

    private final CustomerCallsRepo customerCallsRepo;
    private final CustomerTariffService customerTariffService;
    private final CustomerService customerService;
    private final TariffService tariffService;
    private final CustomerCache customerCache;
    private final CustomerCallsService customerCallsService;
    private final CustomerTimeService timeService;

    /**
     * Создание нового абонента
     * @param event событие создание нового абонента
     */
    @Transactional
    public void createNewCustomer(CreateNewCustomerEvent event) {
        Tariff tariff = tariffService.findByNameOrNull(event.tariff());

        Customer customer = Customer.builder()
                .msisnd(event.msisnd())
                .roles(Collections.singleton(CustomerRole.DEFAULT))
                .balance(BigDecimal.valueOf(event.balance()))
                .password(event.password())
                .tariff(tariff)
                .build();

        Customer saved = customerService.save(customer);
        //обновить кэш
        customerCache.updateCustomerCache(saved);

        log.info("Successfully created new customer {}", customer.getMsisnd());
    }

    /**
     * Смена тарифа абоненту
     * @param event событие смены тарифа
     */
    @Transactional
    public void changeTariff(ChangeTariffEvent event) {
        Tariff tariff = tariffService.findByName(event.tariff());
        Customer customer = customerService.findByMsisnd(event.msisnd());

        //обновим баланс пользователя(мы должны рассчитать его за использование тарифа)
        //не заносим значение в CustomerPayment за текущий месяц, так как туда кладется только автоматическое снятие
        BigDecimal monthlyCost = customer.getTariff().getMonthlyCost();
        if(monthlyCost.compareTo(BigDecimal.ZERO) > 0) {
            customer.setBalance(customer.getBalance().subtract(monthlyCost));
        }
        //смена тарифа
        customer.setTariff(tariff);

        Customer saved = customerService.save(customer);
        customerCache.updateCustomerCache(saved);
        log.info("Customer {} successfully changed tariff {}", customer.getMsisnd(), customer.getTariff().getName());
    }

    /**
     * Обновление баланса абонента
     * @param event событие изменения баланса абонента
     */
    @Transactional
    public void payment(CustomerPaymentEvent event) {
        Customer customer = customerService.findByMsisnd(event.msisnd());

        customer.setBalance(customer.getBalance().add(BigDecimal.valueOf(event.amount())));

        Customer saved = customerService.save(customer);
        customerCache.updateCustomerCache(saved);
        log.info("Customer {} successfully payed {}", customer.getMsisnd(), event.amount());
    }


    /**
     * Обработка калькуляции от HRS
     * @param event данные о калькуляции
     */
    @Transactional
    public void handleCalcData(CdrCalcedEvent event) {
        Customer customer = customerService.findByMsisnd(event.getMsisnd());
        Pair<Integer, Integer> date = TimeUtils.toPair(event.getUnixTime());

        //обновляем время
        if(event.getMinutesAmount() > 0) {
            Tariff tariff = customer.getTariff();
            CustomerCall customerCall = customerCallsService.findCustomerCall(customer, date.first(), date.second());

            if(tariff.getTariffMinutes() != null && tariff.getTariffMinutes().isCommonMinutes()) {
                customerCall.setMinutes(event.getMinutesAmount());
                customerCall.setMinutesOther(event.getMinutesAmount());
            } else {
                if (event.isSameOp()) {
                    customerCall.setMinutes(event.getMinutesAmount());
                } else customerCall.setMinutesOther(customerCall.getMinutes());
            }

            customerCallsRepo.save(customerCall);
        }
        //обновляем баланс
        if(event.getMoneyAmount().compareTo(BigDecimal.ZERO) > 0) {
            customer.setBalance(customer.getBalance().subtract(event.getMoneyAmount()));
            customerService.save(customer);
        }
        //обновляем кэш c данными
        if(event.getMinutesAmount() > 0)
            customerTariffService.updateCustomerData(event.getMsisnd(), date.first(), date.second());
        //расчет по тарифу с игнорированием января
        if(customer.getTariff().getMonthlyCost().compareTo(BigDecimal.ZERO) > 0)
            timeService.updateCustomerLastTime(customer, event.getUnixTime());

        //обновление времени последней cdr
        timeService.updateCommonLastTime(event.getUnixTime());
    }

    /**
     * добавить пользователей в кэш при запуске приложения
     */
    @EventListener(ApplicationReadyEvent.class)
    @Transactional(readOnly = true)
    public void onStarted() {
        List<Customer> all = customerService.findAll();
        all.forEach(customerCache::updateCustomerCache);
    }
}
