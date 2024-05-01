package com.nexign.babybilling.brtservice.service;

import com.nexign.babybilling.brtservice.entity.Customer;
import com.nexign.babybilling.brtservice.entity.CustomerCall;
import com.nexign.babybilling.brtservice.repo.CustomerCallsRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerCallsService {

    private final CustomerCallsRepo repo;

    /**
     * Получение данные о проговоренных минутах в определенном месяце,
     * если данные не найдены в базе, то создается новый объект
     * @param customer - абонент
     * @param year - год
     * @param month - месяц
     * @return данные за месяц
     */
    public CustomerCall findCustomerCall(Customer customer, Integer year, Integer month) {
        CustomerCall data = repo.findByCustomerAndYearAndMonth(customer, year, month).orElse(null);
        if(data == null)
            data = CustomerCall.builder()
                    .customer(customer)
                    .year(year)
                    .month(month)
                    .minutes(0)
                    .minutesOther(0)
                    .build();

        return data;
    }

    public CustomerCall save(CustomerCall call) {
        return repo.save(call);
    }
}
