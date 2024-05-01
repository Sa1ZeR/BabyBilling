package com.nexign.babybilling.payload.dto;

import lombok.Builder;

@Builder
public record CdrPlusEvent(CallType callType, String servedMsisnd, long dateStart, long dateEnd, String tariff, boolean contactedWithSameOp) {
}
