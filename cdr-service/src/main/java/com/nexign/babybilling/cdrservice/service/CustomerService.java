package com.nexign.babybilling.cdrservice.service;

import com.nexign.babybilling.cdrservice.domain.entity.Customer;
import com.nexign.babybilling.cdrservice.repo.CustomerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepo customerRepo;

    /**
     * Поиск абонента по номеру телефона, алгоритм следующий:
     * 1) смотрим абонента в кеше
     * 2) если нет, то ищем в БД
     * 3) если нет в БД, то создаем нового абонента
     * @param phone - номер абонента
     * @return абонент
     */
    @Transactional
    //@Cacheable(value = "customers")
    public Customer findOrCreateCustomer(String phone) {
        Optional<Customer> byPhone = customerRepo.findByPhone(phone);
        if(byPhone.isEmpty()) {
            Customer build = Customer.builder()
                    .phone(phone)
                    .build();

            return customerRepo.save(build);
        }
        return byPhone.get();
    }
}
