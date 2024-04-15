package com.nexign.babybilling.cdrservice.api;

import com.nexign.babybilling.cdrservice.dto.CallTransactionDto;
import com.nexign.babybilling.cdrservice.mapper.CallTransactionMapper;
import com.nexign.babybilling.cdrservice.service.CallTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transaction/")
public class CallTransactionController {

    private final CallTransactionService transactionService;
    private final CallTransactionMapper mapper;

    @Operation(description = "Получить список транзакций")
    @GetMapping("all")
    public List<CallTransactionDto> findAll(@Parameter(description = "Номер абонента") @RequestParam(required = false) String phone,
                                            @Parameter(description = "Тип звонка") @RequestParam(required = false) String callType) {
        return transactionService.findAll(phone, callType).stream().
                map(mapper::map).toList();
    }
}
