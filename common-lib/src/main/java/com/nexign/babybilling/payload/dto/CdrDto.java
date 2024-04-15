package com.nexign.babybilling.payload.dto;

import lombok.Builder;

@Builder
public record CdrDto(CallType callType, String phone1, long dateStart, long dateEnd, String phone2) {
}
