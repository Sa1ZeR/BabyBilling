package com.nexign.babybilling.payload.events;

import lombok.Builder;

@Builder
public record ChangeTariffEvent(String msisnd, String tariff) {
}
