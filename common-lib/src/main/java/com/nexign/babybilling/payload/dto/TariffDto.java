package com.nexign.babybilling.payload.dto;

import lombok.Builder;

@Builder
public record TariffDto(Long id, String name, double price, TariffCallDto callDto, TariffMinutesDto tariffMinutes) {
}
