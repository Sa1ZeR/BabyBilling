package com.nexign.babybilling.cdrservice.mapper;

import com.nexign.babybilling.mapper.Mapper;
import com.nexign.babybilling.payload.dto.CdrDto;
import org.springframework.stereotype.Component;

@Component
public class CdrMapper implements Mapper<CdrDto, String> {

    @Override
    public String map(CdrDto from) {
        return String.format("%s,%s,%s,%s,%s",
                from.callType().getCode(), from.servedMsisnd(), from.dateStart(), from.dateEnd(), from.contactedMsisnd());
    }
}
