package com.nexign.babybilling.payload.dto;

import lombok.Builder;

@Builder
public record CdrDto(CallType callType, String servedMsisnd, long dateStart, long dateEnd, String contactedMsisnd) {
}
