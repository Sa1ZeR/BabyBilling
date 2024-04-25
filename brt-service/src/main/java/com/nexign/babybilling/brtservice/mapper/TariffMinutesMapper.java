package com.nexign.babybilling.brtservice.mapper;

import com.nexign.babybilling.brtservice.entity.TariffMinutes;
import com.nexign.babybilling.mapper.Mapper;
import com.nexign.babybilling.payload.dto.TariffMinutesDto;
import org.springframework.stereotype.Component;

@Component
public class TariffMinutesMapper implements Mapper<TariffMinutes, TariffMinutesDto> {
    @Override
    public TariffMinutesDto map(TariffMinutes from) {
        return TariffMinutesDto.builder()
                .id(from.getId())
                .minutes(from.getMinutes())
                .minutesOther(from.getMinutesOther())
                .build();
    }
}
