package com.nexign.babybilling.hrsservice.services.calc;

import com.nexign.babybilling.payload.dto.CdrPlusEvent;
import com.nexign.babybilling.payload.dto.CustomerDataDto;
import com.nexign.babybilling.payload.dto.TariffDto;
import com.nexign.babybilling.payload.events.CdrCalcedEvent;
import lombok.Setter;

import java.math.BigDecimal;

public abstract class TariffCalc {

    /**
     * Шаблонный метод для расчетов
     * @param cdr
     * @param calced
     */
    public abstract void calc(CdrPlusEvent cdr, CdrCalcedEvent calced);

    /**
     * Высчитывает продолжительность звонка в минутках
     * @param cdr запись
     * @return продолжительность звонка
     */
    public int getDuration(CdrPlusEvent cdr) {
        return (int) Math.ceil((cdr.dateEnd() - cdr.dateStart()) / 60D);
    }

    /**
     * Подсчет времени, которое доступно в рамках
     * @param cdrPlusEvent cdr запись
     * @param dto данные о пользователе
     * @param tariffDto - данные о тарифе
     * @return
     */
    public int getFreeMinutes(CdrPlusEvent cdrPlusEvent, CustomerDataDto dto, TariffDto tariffDto) {
       if(cdrPlusEvent.contactedWithSameOp()) { //если один оператор
           return Math.max(0, tariffDto.tariffMinutes().minutes() - dto.minutes());
       } else {
           return Math.max(0, tariffDto.tariffMinutes().minutesOther() - dto.minutesOther());
       }
    }

    public int getTariffMinutes(CdrPlusEvent cdrPlusEvent, TariffDto tariffDto) {
        if(cdrPlusEvent.contactedWithSameOp()) { //если один оператор
            return tariffDto.tariffMinutes().minutes();
        } else {
            return tariffDto.tariffMinutes().minutesOther();
        }
    }

    /**
     * Подсчет затрат, если есть какая-то подписка
     * @param event - cdr
     * @param tariffDto данные о тарифе
     * @return сумма за минуту
     */
    public BigDecimal getPrice(CdrPlusEvent event, TariffDto tariffDto) {
        switch (event.callType()) {
            case INCOMING -> {
                if(event.contactedWithSameOp()) {
                    return tariffDto.callDto().incomingCallCost();
                } else return tariffDto.callDto().incomingCallCostOther();
            }
            case OUTGOING -> {
                if(event.contactedWithSameOp()) {
                    return tariffDto.callDto().outgoingCallCost();
                } else return tariffDto.callDto().outgoingCallCostOther();
            }
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getPriceWithoutPacket(CdrPlusEvent event, TariffDto tariffDto) {
        switch (event.callType()) {
            case INCOMING -> {
                if(event.contactedWithSameOp()) {
                    return tariffDto.callDto().incomingCallCostWithoutPacket();
                } else return tariffDto.callDto().incomingCallCostOtherWithoutPacket();
            }
            case OUTGOING -> {
                if(event.contactedWithSameOp()) {
                    return tariffDto.callDto().outgoingCallCostWithoutPacket();
                } else return tariffDto.callDto().outgoingCallCostOtherWithoutPacket();
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * Необходимо ли использовать в подсчетах минуты
     * @param event
     * @param tariffDto
     * @return
     */
    public boolean mustCalcMin(CdrPlusEvent event, TariffDto tariffDto) {
        if(tariffDto.tariffMinutes() == null) return false;

        if(event.contactedWithSameOp()) { //если один оператор
            return tariffDto.tariffMinutes().minutes() > 0;
        } else {
            return tariffDto.tariffMinutes().minutesOther() > 0;
        }
    }
}
