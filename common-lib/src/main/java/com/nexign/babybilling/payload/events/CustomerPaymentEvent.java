package com.nexign.babybilling.payload.events;

import lombok.Builder;

@Builder
public record CustomerPaymentEvent(String msisnd, double balance) {
}
