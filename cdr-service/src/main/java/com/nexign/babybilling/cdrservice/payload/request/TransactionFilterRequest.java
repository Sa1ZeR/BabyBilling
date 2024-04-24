package com.nexign.babybilling.cdrservice.payload.request;

import com.nexign.babybilling.payload.dto.CallType;
import io.swagger.v3.oas.annotations.Parameter;

public record TransactionFilterRequest(@Parameter(description = "Номер абонента совершающего звонок") String servedMsisnd,
                                       @Parameter(description = "Номер абонента принимающего звонок") String contactedMsisnd,
                                       @Parameter(description = "Тип звонка") CallType callType,
                                       @Parameter(description = "Номер страницы") Integer page,
                                       @Parameter(description = "Кол-во строк на страницу") Integer size) {
}
