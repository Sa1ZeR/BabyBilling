package com.nexign.babybilling.payload.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TariffCallDto(Long id, BigDecimal incomingCallCost, BigDecimal incomingCallCostOther,
        BigDecimal outgoingCallCost,
        BigDecimal outgoingCallCostOther, BigDecimal incomingCallCostWithoutPacket,
        BigDecimal incomingCallCostOtherWithoutPacket,
        BigDecimal outgoingCallCostWithoutPacket, BigDecimal outgoingCallCostOtherWithoutPacket) {
}
