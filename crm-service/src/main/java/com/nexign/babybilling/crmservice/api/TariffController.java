package com.nexign.babybilling.crmservice.api;

import com.nexign.babybilling.crmservice.facade.TariffFacade;
import com.nexign.babybilling.crmservice.service.ManagerService;
import com.nexign.babybilling.crmservice.service.TariffService;
import com.nexign.babybilling.payload.dto.TariffDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tariff/")
public class TariffController {

    private final TariffFacade tariffFacade;
    private final ManagerService managerService;

    @Operation(description = "Получение информации о тарифе по его имени")
    @GetMapping("{tariff}")
    public TariffDto getTariff(@PathVariable @Parameter(description = "Название тарифа", example = "Классика") String tariff) {
        return tariffFacade.findTariff(tariff);
    }

    @Operation(description = "Получение информации о текущих тарифах")
    @GetMapping("all")
    public List<TariffDto> all() {
        return tariffFacade.findAll();
    }
}
