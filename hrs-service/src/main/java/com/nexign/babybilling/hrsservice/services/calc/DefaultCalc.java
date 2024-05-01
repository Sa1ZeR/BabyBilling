package com.nexign.babybilling.hrsservice.services.calc;

import com.nexign.babybilling.hrsservice.services.CustomerService;
import com.nexign.babybilling.hrsservice.services.TariffService;
import com.nexign.babybilling.payload.dto.CdrPlusEvent;
import com.nexign.babybilling.payload.dto.CustomerDataDto;
import com.nexign.babybilling.payload.dto.TariffDto;
import com.nexign.babybilling.payload.events.CdrCalcedEvent;
import com.nexign.babybilling.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DefaultCalc extends TariffCalc {

    private final TariffService tariffService;
    private final CustomerService customerService;

    @Override
    public void calc(CdrPlusEvent cdr, CdrCalcedEvent calced) {
        LocalDateTime localDateTime = TimeUtils.toLocalDateTime(cdr.dateStart());
        int duration = getDuration(cdr);

        //получение данных из брт
        CustomerDataDto customerData = customerService.findCustomerData(cdr.servedMsisnd(), localDateTime.getYear(), localDateTime.getMonthValue());
        TariffDto tariff = tariffService.findByName(cdr.tariff());

        //только если у абонента есть подписка или какие-либо фичи, в противном случае считает по значениям без подписки
        if(customerData != null) {
            System.out.println("mega calc");

            BigDecimal price = getPrice(cdr, tariff);
            calced.setMoneyAmount(price.multiply(BigDecimal.valueOf(duration)));

            if (mustCalcMin(cdr, tariff)) {
                int freeMinutes = getFreeMinutes(cdr, customerData, tariff);
                //устанавливаем минуты в подсчеты
                calced.setMinutesAmount(Math.min(freeMinutes, duration));

                //остаток
                int rem = duration - freeMinutes;
                if (rem > 0) {
                    defaultCalc(cdr, calced, tariff, rem);
                }
            }

            //если посчитали сумму выше, то дальше идти нет смысла
            if (price.compareTo(BigDecimal.ZERO) > 0) return;
        }

        //установка цены без подписки
        defaultCalc(cdr, calced, tariff, duration);
    }

    private void defaultCalc(CdrPlusEvent cdr, CdrCalcedEvent calced, TariffDto tariff, int duration) {
        BigDecimal price = getPriceWithoutPacket(cdr, tariff);
        calced.setMoneyAmount(price.multiply(BigDecimal.valueOf(duration)));
    }
}
