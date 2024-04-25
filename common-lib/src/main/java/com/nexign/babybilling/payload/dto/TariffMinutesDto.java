package com.nexign.babybilling.payload.dto;

import lombok.Builder;

@Builder
public record TariffMinutesDto(Long id, int minutes, int minutesOther) {
}
