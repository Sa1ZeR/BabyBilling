package com.nexign.babybilling.brtservice.mapper;

import com.nexign.babybilling.brtservice.repo.projection.CustomerDataProjection;
import com.nexign.babybilling.mapper.Mapper;
import com.nexign.babybilling.payload.dto.CustomerDataDto;
import org.springframework.stereotype.Component;

@Component
public class CustomerTariffMapper implements Mapper<CustomerDataProjection, CustomerDataDto> {
    @Override
    public CustomerDataDto map(CustomerDataProjection from) {
        return CustomerDataDto.builder()
                .tariff(from.getTariff())
                .msisnd(from.getMsisnd())
                .minutes(from.getMinutes())
                .minutesOther(from.getMinutesOther())
                .tariffMinutes(from.getTarMin())
                .tariffMinutesOther(from.getTarMinOth())
                .build();
    }
}
