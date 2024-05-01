package com.nexign.babybilling.brtservice.repo;

import com.nexign.babybilling.brtservice.entity.Customer;
import com.nexign.babybilling.brtservice.repo.projection.CustomerDataProjection;
import com.nexign.babybilling.brtservice.repo.projection.CustomerTariffProjection;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer, Long> {

    @EntityGraph(attributePaths = {"tariff", "roles"})
    @Override
    List<Customer> findAll();

    @EntityGraph(attributePaths = {"tariff", "roles"})
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Customer> findByMsisnd(String phone);

    @Query("select c.msisnd as msisnd, t.name as tariff from Customer c join c.tariff t where c.msisnd = :phone")
    Optional<CustomerTariffProjection> findByCustomerInfo(@Param("phone") String phone);

    @Query("select c.msisnd as msisnd, t.name as tariff, t.tariffMinutes.minutes as tarMin, t.tariffMinutes.minutesOther as tarMinOth, cl.minutes as minutes, cl.minutesOther as minutesOther, cl.year as year, cl.month as month from Customer c join c.tariff t join t.tariffMinutes tm left join CustomerCall cl on cl.customer = c where c.msisnd = :msisnd")
    List<CustomerDataProjection> findCustomerData(@Param("msisnd") String msisnd);
}
