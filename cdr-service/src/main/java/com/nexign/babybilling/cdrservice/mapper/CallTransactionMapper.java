package com.nexign.babybilling.cdrservice.mapper;

import com.nexign.babybilling.cdrservice.domain.entity.CallTransaction;
import com.nexign.babybilling.cdrservice.dto.CallTransactionDto;
import com.nexign.babybilling.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class CallTransactionMapper implements Mapper<CallTransaction, CallTransactionDto> {
    @Override
    public CallTransactionDto map(CallTransaction from) {
        return CallTransactionDto.builder()
                .callType(from.getCallType())
                .dateStart(from.getDateStart())
                .dateEnd(from.getDateEnd())
                .servedMsisnd(from.getServedCustomer().getPhone())
                .contactedMsisnd(from.getContactedCustomer().getPhone())
                .build();
    }
}
