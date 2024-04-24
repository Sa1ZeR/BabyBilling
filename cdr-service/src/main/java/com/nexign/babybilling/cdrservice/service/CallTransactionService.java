package com.nexign.babybilling.cdrservice.service;

import com.nexign.babybilling.cdrservice.domain.entity.CallTransaction;
import com.nexign.babybilling.cdrservice.domain.entity.Customer;
import com.nexign.babybilling.cdrservice.dto.CallTransactionDto;
import com.nexign.babybilling.cdrservice.mapper.CallTransactionMapper;
import com.nexign.babybilling.cdrservice.payload.request.TransactionFilterRequest;
import com.nexign.babybilling.cdrservice.repo.CallTransactionFilter;
import com.nexign.babybilling.cdrservice.repo.CallTransactionFilterImpl;
import com.nexign.babybilling.cdrservice.repo.CallTransactionRepo;
import com.nexign.babybilling.payload.dto.CdrDto;
import com.nexign.babybilling.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CallTransactionService {

    private final CallTransactionRepo transactionRepo;
    private final CallTransactionMapper transactionMapper;

    @Transactional
    public CallTransaction save(CdrDto cdrDto, Customer customer1, Customer customer2) {
        return transactionRepo.save(CallTransaction.builder()
                .callType(cdrDto.callType())
                .dateStart(TimeUtils.toLocalDateTime(cdrDto.dateStart()))
                .dateEnd(TimeUtils.toLocalDateTime(cdrDto.dateEnd()))
                .servedCustomer(customer1)
                .contactedCustomer(customer2)
                .build());
    }

    @Transactional(readOnly = true)
    public List<CallTransaction> findAll(String phone, String callType) {
        return transactionRepo.findAll();
    }

    @Transactional(readOnly = true)
    public List<CallTransactionDto> findByFilter(TransactionFilterRequest request) {
        return transactionRepo.findByTransactionFilter(request)
                .stream().map(transactionMapper::map).toList();
    }
}
