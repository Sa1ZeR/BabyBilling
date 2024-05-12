package com.nexign.babybilling.brtservice.service;

import com.nexign.babybilling.brtservice.entity.Customer;
import db.migration.V2__InitData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CronService {

    private final CustomerService customerService;
    private final CustomerPaymentsService customerPaymentsService;

    @Scheduled(fixedDelay = 5000L)
    @Transactional
    public void payment() {
        //пополним баланс раномным абонентам
        for(int i =0; i < 10; i++) {
            String phone = V2__InitData.phones.get((int) (Math.random() * V2__InitData.phones.size()));
            Customer byMsisnd = customerService.findByMsisnd(phone);
            int amount = (int) (Math.random() * 1000 + 100);
            byMsisnd.setBalance(BigDecimal.valueOf(amount));

            customerService.save(byMsisnd);
            log.info("Пополнен баланс для {} на сумму {}", byMsisnd.getMsisnd(), amount);
        }
    }


    @Scheduled(fixedDelay = 5000L)
    @Transactional
    public void calcTariffs() {
        //получение абонентов с тарифами помесячной оплатой
        List<Customer> customers = customerService.findAllWithSubTariff();

        for (Customer customer : customers) {
            customerPaymentsService.calcNotPayed(customer);
        }
    }
}
