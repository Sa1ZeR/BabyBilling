package com.nexign.babybilling.brtservice.repo;

import com.nexign.babybilling.brtservice.entity.Customer;
import com.nexign.babybilling.brtservice.repo.projection.CustomerTariffProjection;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer, Long> {

    @EntityGraph(attributePaths = {"tariff", "roles"})
    @Override
    List<Customer> findAll();

    @EntityGraph(attributePaths = {"tariff", "roles"})
    Optional<Customer> findByMsisnd(String phone);

    @EntityGraph(attributePaths = {"tariff", "roles"})
    Optional<Customer> findByMsisndAndPassword(String phone, String password);

    @Query("select c from Customer c where c.msisnd = :phone")
    Optional<CustomerTariffProjection> findByCustomerInfo(@Param("phone") String phone);
}
