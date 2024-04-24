package com.nexign.babybilling.crmservice.payload.request;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateCustomerRequest(@Parameter(description = "номер абонента", required = true) @Pattern(regexp = "[0-9]{11}") String msisnd,
                                    @Parameter(description = "пароль абонента", required = true) @NotBlank(message = "Пароль не может быть пустым") String password,
                                    @Parameter(description = "тариф", required = true, example = "Классика") @NotBlank(message = "Тариф не может быть пустым") String tariff,
                                    @Parameter(description = "стартовый баланс") Double balance) {
}
