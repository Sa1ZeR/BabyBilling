package com.nexign.babybilling.payload.dto;

import lombok.Builder;

@Builder
public record CdrPlusEvent(CdrDto cdr, TariffDto tariff) {
}
