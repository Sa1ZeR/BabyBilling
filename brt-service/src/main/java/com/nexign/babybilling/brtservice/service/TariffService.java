package com.nexign.babybilling.brtservice.service;

import com.nexign.babybilling.brtservice.entity.Tariff;
import com.nexign.babybilling.brtservice.repo.TariffRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TariffService {

    private final TariffRepo tariffRepo;

    @Transactional(readOnly = true)
    public Tariff findByName(String name) {
        return tariffRepo.findByName(name).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Tariff with name %s not found", name)));
    }

    @Transactional(readOnly = true)
    public Tariff findByNameOrNull(String tariff) {
        return tariffRepo.findByName(tariff).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Tariff> findAll() {
        return tariffRepo.findAll();
    }
}
