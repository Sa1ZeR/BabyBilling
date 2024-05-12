package com.nexign.babybilling.payload.events;

import lombok.Builder;

@Builder
public record CreateNewCustomerEvent(String msisnd, String tariff, double balance, String password) {
}
