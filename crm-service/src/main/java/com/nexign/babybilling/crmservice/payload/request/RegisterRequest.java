package com.nexign.babybilling.crmservice.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(@Pattern(regexp = "[0-9]{11}", message = "Неверный формат номера") String msisnd,
                              @NotBlank(message = "Пароль не может быть пустым") String password) {
}
