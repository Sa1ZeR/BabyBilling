package com.nexign.babybilling.brtservice.mapper;

import com.nexign.babybilling.brtservice.repo.projection.CustomerTariffProjection;
import com.nexign.babybilling.mapper.Mapper;
import com.nexign.babybilling.payload.dto.TariffDto;
import org.springframework.stereotype.Component;

@Component
public class CustomerTariffMapper implements Mapper<CustomerTariffProjection, TariffDto> {
    @Override
    public TariffDto map(CustomerTariffProjection from) {
        return null;
    }
}
