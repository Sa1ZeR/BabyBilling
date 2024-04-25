package com.nexign.babybilling.brtservice.mapper;

import com.nexign.babybilling.brtservice.entity.TariffCalls;
import com.nexign.babybilling.mapper.Mapper;
import com.nexign.babybilling.payload.dto.TariffCallDto;
import org.springframework.stereotype.Component;

@Component
public class TariffCallMapper implements Mapper<TariffCalls, TariffCallDto> {
    @Override
    public TariffCallDto map(TariffCalls from) {
        return TariffCallDto.builder()
                .id(from.getId())
                .incomingCallCost(from.getIncomingCallCost())
                .incomingCallCostOther(from.getIncomingCallCostOther())
                .incomingCallCostWithoutPacket(from.getIncomingCallCostWithoutPacket())
                .incomingCallCostOtherWithoutPacket(from.getIncomingCallCostOtherWithoutPacket())
                .outgoingCallCost(from.getOutgoingCallCost())
                .outgoingCallCostOther(from.getOutgoingCallCostOther())
                .outgoingCallCostWithoutPacket(from.getOutgoingCallCostWithoutPacket())
                .outgoingCallCostOtherWithoutPacket(from.getOutgoingCallCostOtherWithoutPacket())
                .build();
    }
}
