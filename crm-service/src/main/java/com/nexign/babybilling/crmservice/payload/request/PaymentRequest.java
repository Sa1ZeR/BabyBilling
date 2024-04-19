package com.nexign.babybilling.crmservice.payload.request;

import jakarta.validation.constraints.DecimalMin;

public record PaymentRequest(@DecimalMin(value = "0.1") double balance) {
}
