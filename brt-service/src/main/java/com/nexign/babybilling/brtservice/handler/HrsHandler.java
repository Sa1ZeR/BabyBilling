package com.nexign.babybilling.brtservice.handler;

import com.nexign.babybilling.brtservice.facade.CustomerFacade;
import com.nexign.babybilling.payload.events.CdrCalcedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(topics = {"${spring.kafka.topics.calculation-data-topic}"}, containerFactory = "concurrentKafkaJsonListenerContainerFactory")
public class HrsHandler {

    private final CustomerFacade customerFacade;

    @KafkaHandler(isDefault = true)
    public void handleCalcEvent(CdrCalcedEvent event) {
        log.info("Received CdrCalcedEvent {}...", event);
        customerFacade.handleCalcData(event);
    }
}
