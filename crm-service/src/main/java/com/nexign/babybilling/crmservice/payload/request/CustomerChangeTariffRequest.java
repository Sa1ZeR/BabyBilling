package com.nexign.babybilling.crmservice.payload.request;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;

public record CustomerChangeTariffRequest(
        @Parameter(description = "Имя тарифа", required = true, example = "Помесячный") @NotBlank(message = "Тариф не может быть пустым") String tariff) {
}
