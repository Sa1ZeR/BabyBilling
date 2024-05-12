package com.nexign.babybilling.brtservice.service;

import com.nexign.babybilling.brtservice.entity.Customer;
import com.nexign.babybilling.brtservice.entity.CustomerPayment;
import com.nexign.babybilling.brtservice.repo.CustomerPaymentsRepo;
import com.nexign.babybilling.brtservice.repo.CustomerRepo;
import com.nexign.babybilling.domain.Pair;
import com.nexign.babybilling.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerPaymentsService {

    private static final Map<Integer, Integer> MONTH_CHECK = new HashMap<>();

    private final CustomerRepo customerRepo;

    private final CustomerPaymentsRepo repo;
    private final RedisTimeCache redisTimeCache;
    private final CustomerService customerService;

    /**
     * Поиск оплаты на определенный месяц
     * @param customer абонент
     * @param year год
     * @param month - месяц
     * @return оплата на выбранную дату, может вернуть нулл, если такой нет
     */
    @Transactional(readOnly = true)
    public CustomerPayment findByCustomerAndDate(Customer customer, Integer year, Integer month) {
        return repo.findByCustomerAndYearAndMonth(customer, year, month).orElse(null);
    }

    @Transactional()
    public CustomerPayment save(CustomerPayment payment) {
        return repo.save(payment);
    }

    /**
     * Оплата тарифа за все неоплаченные месяца
     * @param customer абонент
     */
    @Transactional
    public void calcNotPayed(Customer customer) {
        //берем дату из самой актуальной cdr
        Integer common = redisTimeCache.getValueByKey(CustomerTimeService.COMMON_KEY);
        if(ObjectUtils.isEmpty(common) || common == 0) return;

        Pair<Integer, Integer> commonDate = TimeUtils.toPair(common);
        Optional<CustomerPayment> payment = repo.findFirstByCustomerOrderByYearDescMonthDesc(customer);
        if(commonDate.second() == 1) return;

        //проверяем, как часто мы получаем одну и ту же дату(необходимо для расчетов 12 месяца)
        Integer count = MONTH_CHECK.getOrDefault(commonDate.second(), 0);
        MONTH_CHECK.put(commonDate.second(), count+1);

        LocalDateTime dateStart = payment.map(customerPayment -> LocalDateTime.of(customerPayment.getYear(), customerPayment.getMonth(), 1, 1, 0))
                .orElseGet(() -> LocalDateTime.of(2024, 1, 1, 1, 0));
        LocalDateTime dateEnd = LocalDateTime.of(commonDate.first(), Math.max(1, count > 5 ? commonDate.second() : commonDate.second()-1), 2, 0, 0);

        log.info("Расчет по {}", dateEnd);
        //для каждого незаполненного месяца создаем оплату (Январь исключаем)
        if(dateEnd.getMonthValue() > 1) {
            while (dateStart.isBefore(dateEnd)) {
                Optional<CustomerPayment> trueCheck = repo.findByCustomerAndYearAndMonth(customer, dateStart.getYear(), dateStart.getMonthValue());
                if(trueCheck.isEmpty()) {
                    //обновление баланса
                    BigDecimal monthlyCost = customer.getTariff().getMonthlyCost();
                    customer.setBalance(customer.getBalance().subtract(monthlyCost));

                    customerService.save(customer);

                    //заносим данные об оплате за месяц
                    repo.save(CustomerPayment.builder()
                            .customer(customer)
                            .year(dateStart.getYear())
                            .month(dateEnd.getMonthValue())
                            .amount(monthlyCost)
                            .build());

                    log.info("{}: Оплата тарифа за {}.{}, сумма {}", customer.getMsisnd(), dateStart.getYear(), dateStart.getMonth().getValue(), monthlyCost);
                }
                dateStart = dateStart.plusMonths(1);
            }
        }
    }
}
