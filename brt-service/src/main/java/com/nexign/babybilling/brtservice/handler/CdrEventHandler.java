package com.nexign.babybilling.brtservice.handler;

import com.nexign.babybilling.brtservice.service.BrtService;
import com.nexign.babybilling.payload.dto.CdrDto;
import com.nexign.babybilling.payload.dto.CdrPlusEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(topics = {"${spring.kafka.topics.cdr-file-topic}"}, containerFactory = "concurrentKafkaListenerContainerFactory")
public class CdrEventHandler {

    private final BrtService brtService;

    /**
     * Слушает топик, который отвечает за передачу файлов
     * @param s сообщение
     */
    @KafkaHandler
    public void handleCdrFileTopic(String s) {
        List<CdrDto> data = brtService.readCdrFile(s);
        List<CdrPlusEvent> cdrPlusData = brtService.filterCdrData(data);
        brtService.calculateCustomers(cdrPlusData);
    }
}
