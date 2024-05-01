package com.nexign.babybilling.brtservice.facade;

import com.nexign.babybilling.CustomerRole;
import com.nexign.babybilling.brtservice.entity.Customer;
import com.nexign.babybilling.brtservice.entity.CustomerCall;
import com.nexign.babybilling.brtservice.entity.Tariff;
import com.nexign.babybilling.brtservice.repo.CustomerCallsRepo;
import com.nexign.babybilling.brtservice.service.CustomerCallsService;
import com.nexign.babybilling.brtservice.service.CustomerTariffService;
import com.nexign.babybilling.brtservice.service.cache.CustomerCache;
import com.nexign.babybilling.brtservice.service.CustomerService;
import com.nexign.babybilling.brtservice.service.TariffService;
import com.nexign.babybilling.payload.events.CdrCalcedEvent;
import com.nexign.babybilling.payload.events.ChangeTariffEvent;
import com.nexign.babybilling.payload.events.CreateNewCustomerEvent;
import com.nexign.babybilling.payload.events.CustomerPaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerFacade {
    private final CustomerCallsRepo customerCallsRepo;
    private final CustomerTariffService customerTariffService;
    private final CustomerService customerService;
    private final TariffService tariffService;
    private final CustomerCache customerCache;
    private final CustomerCallsService customerCallsService;

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
                .password(event.passwrd())
                .tariff(tariff)
                .build();

        Customer saved = customerService.save(customer);
        //обновить кэш
        customerCache.updateCustomerCache(saved);
    }

    /**
     * Смена тарифа абоненту
     * @param event событие смены тарифа
     */
    @Transactional
    public void changeTariff(ChangeTariffEvent event) {
        Tariff tariff = tariffService.findByName(event.tariff());
        Customer customer = customerService.findByMsisnd(event.tariff());

        customer.setTariff(tariff);

        Customer saved = customerService.save(customer);
        customerCache.updateCustomerCache(saved);
    }

    /**
     * Обновление баланса абонента
     * @param event событие изменения баланса абонента
     */
    @Transactional
    public void payment(CustomerPaymentEvent event) {
        Customer customer = customerService.findByMsisnd(event.msisnd());

        customer.setBalance(BigDecimal.valueOf(event.amount()));

        Customer saved = customerService.save(customer);
        customerCache.updateCustomerCache(saved);
    }


    /**
     * Обработка калькуляции от HRS
     * @param event данные о калькуляции
     */
    @Transactional
    public void handleCalcData(CdrCalcedEvent event) {
        Customer customer = customerService.findByMsisnd(event.getMsisnd());

        //обновляем время
        if(event.getMinutesAmount() > 0) {

            Tariff tariff = customer.getTariff();
            CustomerCall customerCall = customerCallsService.findCustomerCall(customer, event.getYear(), event.getMonth());
            System.out.println(1111);
            if(tariff.getTariffMinutes().isCommonMinutes()) {
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
        //обновляем кэш
        if(event.getMinutesAmount() > 0)
            customerTariffService.updateCustomerData(event.getMsisnd(), event.getYear(), event.getMonth());
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
