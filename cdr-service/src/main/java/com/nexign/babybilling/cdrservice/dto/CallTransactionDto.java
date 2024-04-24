package com.nexign.babybilling.cdrservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexign.babybilling.payload.dto.CallType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CallTransactionDto(CallType callType, String servedMsisnd,
                                 @JsonFormat(pattern = "yyy.MM.dd hh:mm:ss") LocalDateTime dateStart,
                                 @JsonFormat(pattern = "yyy.MM.dd hh:mm:ss") LocalDateTime dateEnd,
                                 String contactedMsisnd) {
}
