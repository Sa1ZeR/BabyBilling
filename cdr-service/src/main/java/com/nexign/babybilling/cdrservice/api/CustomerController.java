package com.nexign.babybilling.cdrservice.api;

import com.nexign.babybilling.cdrservice.domain.entity.Customer;
import com.nexign.babybilling.cdrservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer/")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/all")
    public List<Customer> findAll() {
        return customerService.findAll();
    }
}
