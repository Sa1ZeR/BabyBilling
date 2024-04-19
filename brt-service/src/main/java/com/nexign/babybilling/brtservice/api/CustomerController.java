package com.nexign.babybilling.brtservice.api;

import com.nexign.babybilling.brtservice.mapper.CustomerMapper;
import com.nexign.babybilling.brtservice.service.CustomerService;
import com.nexign.babybilling.payload.dto.CustomerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer/")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @GetMapping("{msisnd}")
    public CustomerDto findCustomer(@PathVariable String msisnd) {
        return customerMapper.map(customerService.findByMsisnd(msisnd));
    }

    @GetMapping("")
    public CustomerDto findCustomerWithPassword(@RequestParam String msisnd,
                                                @RequestParam String password) {
        return customerMapper.map(customerService.findByMsisndAndPassword(msisnd, password));
    }
}
