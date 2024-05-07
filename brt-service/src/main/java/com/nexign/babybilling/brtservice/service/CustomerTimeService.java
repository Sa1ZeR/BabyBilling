package com.nexign.babybilling.brtservice.service;

import com.nexign.babybilling.brtservice.entity.Customer;
import com.nexign.babybilling.brtservice.entity.CustomerPayment;
import com.nexign.babybilling.domain.Pair;
import com.nexign.babybilling.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerTimeService {

    public static final String COMMON_KEY = "common";

    private final RedisTimeCache redisTimeCache;
    private final CustomerPaymentsService customerPaymentsService;
    private final CustomerService customerService;

    /**
     * Обновляем последнюю дату при необходимости для абонента
     * @param customer абонент
     * @param unixTime время в юникс формате
     * @return статус обновления
     */
    @Transactional()
    public boolean updateCustomerLastTime(Customer customer, Long unixTime) {
        Pair<Integer, Integer> date = TimeUtils.toPair(unixTime);
        if(date.second() == 1) return true;

        //если значение уже есть, то ничего считать не надо
        if(!redisTimeCache.updateValue(customer.getMsisnd(), unixTime)) return true;

        //отнимаем 1, так как нам надо проверить оплату за прошлый месяц
        CustomerPayment payment = customerPaymentsService.findByCustomerAndDate(customer, date.first(), date.second() - 1);
        if(!ObjectUtils.isEmpty(payment)) return true; //если оплата есть

        BigDecimal monthlyCost = customer.getTariff().getMonthlyCost();
        customer.setBalance(customer.getBalance().subtract(monthlyCost));
        customerService.save(customer);
        customerPaymentsService.save(CustomerPayment.builder()
                        .customer(customer)
                        .year(date.first())
                        .month(date.second() -1)
                        .amount(monthlyCost)
                .build());

        log.info("Списание оплаты {} за {}.{}...", monthlyCost, date.first(), date.second() -1);

        return true;
    }

    /**
     * Обновляем при необходимости общее время для всех
     * @param unixTime юникс время
     * @return статус обновления
     */
    public boolean updateCommonLastTime(long unixTime) {
        return redisTimeCache.updateValue(COMMON_KEY, unixTime);
    }
}
