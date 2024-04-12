package com.nexign.babybilling.cdrservice.facade;

import com.nexign.babybilling.cdrservice.domain.entity.Customer;
import com.nexign.babybilling.cdrservice.service.CallTransactionService;
import com.nexign.babybilling.cdrservice.service.cdr.CdrBufferService;
import com.nexign.babybilling.cdrservice.service.CustomerService;
import com.nexign.babybilling.payload.dto.CdrDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CdrTransactionFacade {

    private final CdrBufferService cdrBufferService;
    private final CustomerService customerService;
    private final CallTransactionService callTransactionService;

    /**
     * обработка приходящей cdr записи
     * @param cdrDto - cdr запись
     */
    @Transactional
    public void handleCallTransaction(CdrDto cdrDto) {
        //добавляем в буффер, так как чтение из партиций может идти не в хронологическом порядке
        cdrBufferService.addToBuffer(cdrDto);

        Customer customer1 = customerService.findOrCreateCustomer(cdrDto.phone1());
        Customer customer2 = customerService.findOrCreateCustomer(cdrDto.phone2());

        callTransactionService.save(cdrDto, customer1, customer2);
    }
}
