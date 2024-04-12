package com.nexign.babybilling.cdrservice.service;

import com.nexign.babybilling.cdrservice.service.cdr.CdrBufferService;
import com.nexign.babybilling.cdrservice.service.cdr.CdrService;
import com.nexign.babybilling.payload.dto.CdrDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CronService {

    private final CdrBufferService cdrBufferService;
    private final CdrService cdrService;

    /**
     * Создание файлов из cdr записи и последующая отправка в brt
     */
    @Scheduled(fixedDelay = 100L)
    public void cdrFileCron() {
        List<CdrDto> cdrs = cdrBufferService.getFromBuffer(10);

        if(cdrs.isEmpty()) return;

        Path cdrFile = cdrService.createCdrFile(cdrs);
        cdrService.sendFile(cdrFile);
    }
}
