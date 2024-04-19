package com.nexign.babybilling.crmservice.payload.request;

import jakarta.validation.constraints.NotBlank;

public record ChangeTariffRequest(@NotBlank(message = "Номер абонента не может быть пустым") String msisnd,
                                  @NotBlank(message = "Тариф не может быть пустым") String tariff) {
}
