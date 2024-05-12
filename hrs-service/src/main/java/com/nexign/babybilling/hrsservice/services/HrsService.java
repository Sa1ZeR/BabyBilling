package com.nexign.babybilling.hrsservice.services;

import com.nexign.babybilling.hrsservice.config.property.KafkaProducerProperty;
import com.nexign.babybilling.payload.dto.CdrPlusEvent;
import com.nexign.babybilling.payload.events.CdrCalcedEvent;
import com.nexign.babybilling.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class HrsService {

    private final KafkaProducerProperty producerConfig;
    private final KafkaTemplate<String, CdrCalcedEvent> kafkaTemplate;

    public CdrCalcedEvent buildCalced(CdrPlusEvent event) {
        return CdrCalcedEvent.builder()
                .isSameOp(event.contactedWithSameOp())
                .unixTime(event.dateStart())
                .msisnd(event.servedMsisnd())
                .moneyAmount(BigDecimal.ZERO)
                .minutesAmount(0)
                .build();
    }

    public void sendCalcedData(CdrCalcedEvent event) {
        try {
            SendResult<String, CdrCalcedEvent> result = kafkaTemplate.send(producerConfig.calcDataTopic, event.getMsisnd(), event).get();
            log.info("Successfully sent calced data {}-{}", result.getRecordMetadata().topic(), result.getRecordMetadata().partition());
        } catch (InterruptedException | ExecutionException e) {
            log.error("Can't send calced data: ", e);
        }
    }
}
