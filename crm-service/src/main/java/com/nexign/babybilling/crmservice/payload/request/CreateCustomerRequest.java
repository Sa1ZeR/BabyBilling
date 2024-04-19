package com.nexign.babybilling.crmservice.payload.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateCustomerRequest(@Pattern(regexp = "[0-9]{11}") String msisnd,
                                    @NotBlank(message = "Пароль не может быть пустым") String password,
                                    @NotBlank(message = "Тариф не может быть пустым") String tariff,
                                    @DecimalMin(value = "0.1") Double balance) {
}
