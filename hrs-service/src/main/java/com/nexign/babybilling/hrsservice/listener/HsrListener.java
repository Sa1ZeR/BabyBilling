package com.nexign.babybilling.hrsservice.listener;

import com.nexign.babybilling.hrsservice.falace.HRSCalcFacade;
import com.nexign.babybilling.payload.dto.CdrPlusEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@KafkaListener(topics = {"${spring.kafka.topics.hrs-topic}"}, containerFactory = "concurrentKafkaListenerContainerFactory")
public class HsrListener {

    private final HRSCalcFacade hrsCalcFacade;

    @KafkaHandler
    public void handleCdrPlusData(CdrPlusEvent event) {
        log.info("Successfully received {}", event);
        hrsCalcFacade.calcCdrPlus(event);
    }
}
