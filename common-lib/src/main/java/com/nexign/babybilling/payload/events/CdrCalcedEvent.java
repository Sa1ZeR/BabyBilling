package com.nexign.babybilling.payload.events;

import com.nexign.babybilling.payload.dto.CallType;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public final class CdrCalcedEvent {
    private boolean isSameOp;
    private String msisnd;
    private BigDecimal moneyAmount;
    private Integer minutesAmount; //for subscribe
    private long unixTime;

}
