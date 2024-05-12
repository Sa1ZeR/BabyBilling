package com.nexign.babybilling.brtservice.repo;

import com.nexign.babybilling.brtservice.entity.Customer;
import com.nexign.babybilling.brtservice.entity.CustomerPayment;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface CustomerPaymentsRepo extends JpaRepository<CustomerPayment, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<CustomerPayment> findByCustomerAndYearAndMonth(Customer customer, Integer year, Integer month);

    Optional<CustomerPayment> findFirstByCustomerOrderByYearDescMonthDesc(Customer customer);
}
