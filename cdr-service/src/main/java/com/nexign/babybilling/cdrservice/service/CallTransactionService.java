package com.nexign.babybilling.cdrservice.service;

import com.nexign.babybilling.cdrservice.domain.entity.CallTransaction;
import com.nexign.babybilling.cdrservice.domain.entity.Customer;
import com.nexign.babybilling.cdrservice.dto.CallTransactionDto;
import com.nexign.babybilling.cdrservice.repo.CallTransactionRepo;
import com.nexign.babybilling.payload.dto.CdrDto;
import com.nexign.babybilling.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CallTransactionService {

    private final CallTransactionRepo transactionRepo;

    @Transactional
    public CallTransaction save(CdrDto cdrDto, Customer customer1, Customer customer2) {
        return transactionRepo.save(CallTransaction.builder()
                .callType(cdrDto.callType())
                .dateStart(TimeUtils.toLocalDateTime(cdrDto.dateStart()))
                .dateEnd(TimeUtils.toLocalDateTime(cdrDto.dateEnd()))
                .firstCustomer(customer1)
                .secondCustomer(customer2)
                .build());
    }

    public List<CallTransaction> findAll(String phone, String callType) {
        return null;
    }
}
