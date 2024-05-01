package com.nexign.babybilling.hrsservice.falace;

import com.nexign.babybilling.hrsservice.services.HrsService;
import com.nexign.babybilling.hrsservice.services.calc.TariffCalc;
import com.nexign.babybilling.payload.dto.CdrPlusEvent;
import com.nexign.babybilling.payload.events.CdrCalcedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HRSCalcFacade {

    private final TariffCalc tariffCalc;
    private final HrsService hrsService;

    /**
     * Расчет и отправка данных
     * @param event cdr запись
     */
    public void calcCdrPlus(CdrPlusEvent event) {
        CdrCalcedEvent calced = hrsService.buildCalced(event);

        tariffCalc.calc(event, calced);

        hrsService.sendCalcedData(calced);
    }
}
