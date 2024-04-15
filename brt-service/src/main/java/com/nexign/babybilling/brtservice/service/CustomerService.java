package com.nexign.babybilling.brtservice.service;

import com.nexign.babybilling.brtservice.entity.Customer;
import com.nexign.babybilling.brtservice.repo.CustomerRepo;
import com.nexign.babybilling.brtservice.repo.projection.CustomerTariffProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepo repo;

    @Transactional
    public boolean isCustomer(String phone) {

    }

    public Optional<CustomerTariffProjection> findCustomerInfo(String phone) {
        return repo.findByPhone(phone);
    }
}
