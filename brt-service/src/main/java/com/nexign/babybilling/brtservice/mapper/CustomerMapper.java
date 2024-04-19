package com.nexign.babybilling.brtservice.mapper;

import com.nexign.babybilling.brtservice.entity.Customer;
import com.nexign.babybilling.mapper.Mapper;
import com.nexign.babybilling.payload.dto.CustomerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerMapper implements Mapper<Customer, CustomerDto> {

    private final CustomerTariffMapper customerTariffMapper;
    @Override
    public CustomerDto map(Customer from) {
        return CustomerDto.builder()
                .msisnd(from.getMsisnd())
                .balance(from.getBalance())
                .roles(from.getRoles())
                .build();
    }
}
