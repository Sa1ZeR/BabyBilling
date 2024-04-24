package com.nexign.babybilling.cdrservice.repo;

import com.nexign.babybilling.cdrservice.domain.entity.CallTransaction;
import com.nexign.babybilling.cdrservice.payload.request.TransactionFilterRequest;

import java.util.List;

public interface CallTransactionFilter {

    List<CallTransaction> findByTransactionFilter(TransactionFilterRequest filter);
}
