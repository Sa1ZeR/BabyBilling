package com.nexign.babybilling.crmservice.payload.request;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(@Parameter(description = "Номер абонента", required = true) @Pattern(regexp = "[0-9]{11}", message = "Неверный формат номера") String msisnd,
                              @Parameter (description = "Пароль", required = true)@NotBlank(message = "Пароль не может быть пустым") String password) {
}
