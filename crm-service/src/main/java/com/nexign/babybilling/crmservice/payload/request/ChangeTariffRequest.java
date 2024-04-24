package com.nexign.babybilling.crmservice.payload.request;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;

public record ChangeTariffRequest(@Parameter(required = true, description = "Номер абонента") @NotBlank(message = "Номер абонента не может быть пустым") String msisnd,
                                  @Parameter(description = "нНазвание тарифа", example = "Помесячный", required = true)@NotBlank(message = "Тариф не может быть пустым") String tariff) {
}
