package com.nexign.babybilling.crmservice.api;

import com.nexign.babybilling.crmservice.payload.request.ChangeTariffRequest;
import com.nexign.babybilling.crmservice.payload.request.CreateCustomerRequest;
import com.nexign.babybilling.crmservice.service.ManagerService;
import com.nexign.babybilling.payload.events.ChangeTariffEvent;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager/")
@PreAuthorize(value = "MANAGER")
public class ManagerController {

    private final ManagerService managerService;

    @Operation(description = "Создание нового абонента")
    @PostMapping("create-cutomer")
    public ResponseEntity<String> createCustomer(@Valid @RequestBody @ParameterObject CreateCustomerRequest request) {
        managerService.createCustomer(request);
        return ResponseEntity.ok("Новый абонент успешно создан!");
    }

    @Operation(description = "Смена тарифа абоненту")
    @PostMapping("change-tariff")
    public ResponseEntity<String> changeTariff(@Valid @RequestBody @ParameterObject ChangeTariffRequest request) {
        managerService.changeTariff(request);
        return ResponseEntity.ok("Тариф успешно изменен!");
    }
}
