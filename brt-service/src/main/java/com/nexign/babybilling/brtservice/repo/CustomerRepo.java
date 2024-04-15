package com.nexign.babybilling.brtservice.repo;

import com.nexign.babybilling.brtservice.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
    Optional<Customer> findByPhone(String phone);
}
