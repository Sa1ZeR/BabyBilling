package com.nexign.babybilling.crmservice.facade;

import com.nexign.babybilling.crmservice.service.TariffService;
import com.nexign.babybilling.payload.dto.TariffDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TariffFacade {

    private final TariffService tariffService;

    public List<TariffDto> findAll() {
        return tariffService.findAll();
    }

    public TariffDto findTariff(String tariff) {
        TariffDto dto = tariffService.findByName(tariff);
        if(ObjectUtils.isEmpty(dto)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Тариф не найден");
        return dto;
    }
}
