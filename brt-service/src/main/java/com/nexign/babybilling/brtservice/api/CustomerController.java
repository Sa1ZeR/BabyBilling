package com.nexign.babybilling.brtservice.api;

import com.nexign.babybilling.brtservice.mapper.CustomerMapper;
import com.nexign.babybilling.brtservice.service.CustomerService;
import com.nexign.babybilling.brtservice.service.CustomerTariffService;
import com.nexign.babybilling.payload.dto.CustomerDataDto;
import com.nexign.babybilling.payload.dto.CustomerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer/")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;
    private final CustomerTariffService customerTariffService;

    @GetMapping("{msisnd}")
    public CustomerDto findCustomer(@PathVariable String msisnd) {
        return customerMapper.map(customerService.findByMsisnd(msisnd));
    }

    @GetMapping("")
    @Transactional(readOnly = true)
    public CustomerDto findCustomerWithPassword(@RequestParam String msisnd,
                                                @RequestParam String password) {
        return customerMapper.map(customerService.findByMsisndAndPassword(msisnd, password));
    }

    @GetMapping("data/{msisnd}")
    public CustomerDataDto getCustomerData(@PathVariable String msisnd,
                                           @RequestParam Integer year,
                                           @RequestParam Integer month) {
        return customerTariffService.findCustomerDataFromCache(msisnd, year, month);
    }
}
