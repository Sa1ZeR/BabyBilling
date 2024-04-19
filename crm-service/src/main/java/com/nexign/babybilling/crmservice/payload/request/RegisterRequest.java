package com.nexign.babybilling.crmservice.payload.request;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(@NotBlank(message = "Номер не может быть пустым") String msisnd,
                              @NotBlank(message = "Пароль не может быть пустым") String password) {
}
