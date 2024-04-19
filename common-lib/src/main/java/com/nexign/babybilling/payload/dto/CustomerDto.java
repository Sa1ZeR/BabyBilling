package com.nexign.babybilling.payload.dto;

import com.nexign.babybilling.CustomerRole;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Collection;

@Builder
public record CustomerDto(String msisnd, Collection<CustomerRole> roles, BigDecimal balance, TariffDto tariff) {
}
