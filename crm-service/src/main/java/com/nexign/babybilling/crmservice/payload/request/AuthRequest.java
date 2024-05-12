package com.nexign.babybilling.crmservice.payload.request;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;

public record AuthRequest(@Parameter(description = "Номер абонента", required = true) @NotBlank(message = "Номер не может быть пустым") String msisnd,
                          @Parameter(description = "Пароль абонента", required = true) @NotBlank(message = "Пароль не может быть пустым") String password) {
}
