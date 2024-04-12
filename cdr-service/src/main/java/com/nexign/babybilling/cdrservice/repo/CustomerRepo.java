package com.nexign.babybilling.cdrservice.repo;

import com.nexign.babybilling.cdrservice.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer, Long> {

    Optional<Customer> findByPhone(String phone);
}
