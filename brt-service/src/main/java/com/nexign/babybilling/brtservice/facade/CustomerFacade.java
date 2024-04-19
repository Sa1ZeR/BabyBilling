package com.nexign.babybilling.brtservice.facade;

import com.nexign.babybilling.CustomerRole;
import com.nexign.babybilling.brtservice.entity.Customer;
import com.nexign.babybilling.brtservice.entity.Tariff;
import com.nexign.babybilling.brtservice.service.CustomerService;
import com.nexign.babybilling.brtservice.service.TariffService;
import com.nexign.babybilling.payload.events.ChangeTariffEvent;
import com.nexign.babybilling.payload.events.CreateNewCustomerEvent;
import com.nexign.babybilling.payload.events.CustomerPaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerFacade {

    private final CustomerService customerService;
    private final TariffService tariffService;

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

        customerService.save(customer);
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

        customerService.save(customer);
    }

    /**
     * Обновление баланса абонента
     * @param event событие изменения баланса абонента
     */
    @Transactional
    public void payment(CustomerPaymentEvent event) {
        Customer customer = customerService.findByMsisnd(event.msisnd());

        customer.setBalance(BigDecimal.valueOf(event.balance()));

        customerService.save(customer);
    }
}
