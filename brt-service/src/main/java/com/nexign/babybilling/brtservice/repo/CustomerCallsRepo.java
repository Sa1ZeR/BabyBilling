package com.nexign.babybilling.brtservice.repo;

import com.nexign.babybilling.brtservice.entity.Customer;
import com.nexign.babybilling.brtservice.entity.CustomerCall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerCallsRepo extends JpaRepository<CustomerCall, Long> {

    Optional<CustomerCall> findByCustomer(Customer customer);
    Optional<CustomerCall> findByCustomerAndYearAndMonth(Customer customer, Integer year, Integer month);
}
