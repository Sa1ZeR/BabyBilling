package com.nexign.babybilling.brtservice.mapper;

import com.nexign.babybilling.brtservice.entity.Tariff;
import com.nexign.babybilling.mapper.Mapper;
import com.nexign.babybilling.payload.dto.TariffDto;
import org.springframework.stereotype.Component;

@Component
public class TariffMapper implements Mapper<Tariff, TariffDto> {
    @Override
    public TariffDto map(Tariff from) {
        return null;
    }
}
