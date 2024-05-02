package com.nexign.babybilling.crmservice.api;

import com.nexign.babybilling.crmservice.facade.CustomerFacade;
import com.nexign.babybilling.crmservice.payload.request.CustomerChangeTariffRequest;
import com.nexign.babybilling.crmservice.payload.request.PaymentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize(value = "hasAuthority('DEFAULT')")
@RequestMapping("/api/customer/")
public class CustomerController {

    private final CustomerFacade customerFacade;

    @Operation(description = "Пополнение баланса")
    @PostMapping("{msisnd}/payment")
    public ResponseEntity<String> payment(@PathVariable @Parameter(description = "номер абонента") String msisnd,
                                          @Valid @RequestBody @ParameterObject PaymentRequest request) {
        customerFacade.payment(msisnd, request);
        return ResponseEntity.ok("Баланс успешно пополнен");
    }

    @Operation(description = "Смена тарифа")
    @PostMapping("{msisnd}/change-tariff")
    public ResponseEntity<String> changeTariff(@PathVariable @Parameter(description = "номер абонента") String msisnd,
                                          @Valid @RequestBody @ParameterObject CustomerChangeTariffRequest request) {
        customerFacade.changeTarrif(msisnd, request);
        return ResponseEntity.ok("Тариф успешно изменен");
    }
}
