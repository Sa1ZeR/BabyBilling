package com.nexign.babybilling.cdrservice.repo;

import com.nexign.babybilling.cdrservice.domain.entity.CallTransaction;
import com.nexign.babybilling.cdrservice.domain.entity.QCallTransaction;
import com.nexign.babybilling.cdrservice.domain.entity.QCustomer;
import com.nexign.babybilling.cdrservice.payload.request.TransactionFilterRequest;
import com.nexign.babybilling.cdrservice.utils.QPredicates;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.nexign.babybilling.cdrservice.domain.entity.QCallTransaction.callTransaction;

@RequiredArgsConstructor
public class CallTransactionFilterImpl implements CallTransactionFilter{

    private final EntityManager entityManager;

    /**
     * получение транзакций на основе фильтра
     * @param filter фильтр
     * @return список транзакций
     */
    @Override
    public List<CallTransaction> findByTransactionFilter(TransactionFilterRequest filter) {
        Predicate predicate = QPredicates.builder().add(filter.callType(), callTransaction.callType::eq).build();

        JPAQuery<CallTransaction> query = new JPAQuery<>(entityManager)
                .select(callTransaction)
                .from(callTransaction);

        if(filter.contactedMsisnd() != null) {
            query.join(callTransaction.contactedCustomer)
                    .where(callTransaction.contactedCustomer.phone.eq(filter.contactedMsisnd()));
        }
        if(filter.servedMsisnd() != null)
            query.join(callTransaction.servedCustomer)
                    .where(callTransaction.servedCustomer.phone.eq(filter.servedMsisnd()));

        if(filter.size() != null)
            query.limit(filter.size());
        if(filter.page() != null)
            query.offset(Math.max(0, filter.page()-1));

        return query.where(predicate).fetch();
    }
}
