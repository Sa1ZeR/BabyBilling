package com.nexign.babybilling.payload.dto;

import lombok.Builder;

@Builder
public record CustomerDataDto(String msisnd, String tariff, Integer year, Integer month, Integer minutes, Integer minutesOther, Integer tariffMinutes, Integer tariffMinutesOther) {
}
