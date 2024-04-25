package com.nexign.babybilling.brtservice.mapper;

import com.nexign.babybilling.brtservice.entity.Tariff;
import com.nexign.babybilling.mapper.Mapper;
import com.nexign.babybilling.payload.dto.TariffDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TariffMapper implements Mapper<Tariff, TariffDto> {

    private final TariffCallMapper callMapper;
    private final TariffMinutesMapper minutesMapper;

    @Override
    public TariffDto map(Tariff from) {
        return TariffDto.builder()
                .id(from.getId())
                .name(from.getName())
                .price(from.getMonthlyCost().doubleValue())
                .callDto(from.getTariffCalls() != null ? callMapper.map(from.getTariffCalls()) : null)
                .tariffMinutes(from.getTariffMinutes() != null ? minutesMapper.map(from.getTariffMinutes()) : null)
                .build();
    }
}
