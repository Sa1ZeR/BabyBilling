package com.nexign.babybilling.brtservice.repo;

import com.nexign.babybilling.brtservice.entity.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TariffRepo extends JpaRepository<Tariff, Long> {
    Optional<Tariff> findByName(String name);
}
