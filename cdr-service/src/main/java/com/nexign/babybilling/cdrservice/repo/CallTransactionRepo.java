package com.nexign.babybilling.cdrservice.repo;

import com.nexign.babybilling.cdrservice.domain.entity.CallTransaction;
import com.nexign.babybilling.cdrservice.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CallTransactionRepo extends JpaRepository<CallTransaction, Long>, CallTransactionFilter {

}
