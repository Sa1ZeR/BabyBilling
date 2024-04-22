package com.nexign.babybilling.brtservice.service;

import com.nexign.babybilling.brtservice.entity.Customer;
import com.nexign.babybilling.brtservice.mapper.CustomerMapper;
import com.nexign.babybilling.payload.dto.CustomerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerCache {

    private final CustomerMapper customerMapper;

    /**
     * Обновить абонента в кэше
     * @param customer абонент в виде сущности
     * @return dto абонента
     */
    @CachePut(cacheNames = "customerCache", key = "#customer.msisnd")
    public CustomerDto updateCustomerCache(Customer customer) {
        return  customerMapper.map(customer);
    }
}
