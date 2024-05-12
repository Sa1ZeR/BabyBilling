package com.nexign.babybilling.crmservice.payload.request;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.DecimalMin;

public record PaymentRequest(@Parameter(description = "сумма пополнения", required = true) @DecimalMin(value = "0.1") double amount) {
}
