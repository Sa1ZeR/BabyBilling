package com.nexign.babybilling.brtservice.mapper;

import com.nexign.babybilling.mapper.Mapper;
import com.nexign.babybilling.payload.dto.CallType;
import com.nexign.babybilling.payload.dto.CdrDto;
import com.nexign.babybilling.payload.dto.CdrPlusEvent;
import org.springframework.stereotype.Component;

@Component
public class CdrMapper implements Mapper<String, CdrDto> {
    @Override
    public CdrDto map(String from) {
        String[] splitArr = from.split(",");

        if (splitArr.length != 5)
            throw new IllegalArgumentException(String.format("Can't parse cdr data %s", from));

        CallType callType = CallType.getType(splitArr[0]);
        String phone1 = splitArr[1];
        String phone2 = splitArr[4];

        long dateEnd;
        long dateStart;
        try {
            dateStart = Long.parseLong(splitArr[2]);
            dateEnd = Long.parseLong(splitArr[3]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Can't parse cdr data %s", from));
        }

        return CdrDto.builder()
                .callType(callType)
                .servedMsisnd(phone1)
                .contactedMsisnd(phone2)
                .dateStart(dateStart)
                .dateEnd(dateEnd)
                .build();
    }
}
