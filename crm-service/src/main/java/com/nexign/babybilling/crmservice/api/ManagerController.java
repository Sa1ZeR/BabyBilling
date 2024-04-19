package com.nexign.babybilling.crmservice.api;

import com.nexign.babybilling.crmservice.payload.request.ChangeTariffRequest;
import com.nexign.babybilling.crmservice.payload.request.CreateCustomerRequest;
import com.nexign.babybilling.crmservice.service.ManagerService;
import com.nexign.babybilling.payload.events.ChangeTariffEvent;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    @PostMapping("create-cutomer")
    public ResponseEntity<String> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        managerService.createCustomer(request);
        return ResponseEntity.ok("Новый абонент успешно создан!");
    }

    @PostMapping("changeTariff")
    public ResponseEntity<String> changeTariff(@Valid @RequestBody ChangeTariffRequest request) {
        managerService.changeTariff(request);
        return ResponseEntity.ok("Тариф успешно изменен!");
    }
}
