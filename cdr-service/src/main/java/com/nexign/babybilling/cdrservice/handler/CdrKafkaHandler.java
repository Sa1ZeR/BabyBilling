package com.nexign.babybilling.cdrservice.handler;

import com.nexign.babybilling.cdrservice.facade.CdrTransactionFacade;
import com.nexign.babybilling.cdrservice.service.CallTransactionService;
import com.nexign.babybilling.payload.dto.CdrDto;
import com.nexign.babybilling.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(topics = "${spring.kafka.consumer.cdr-generator-topic-name}", containerFactory = "concurrentKafkaListenerContainerFactory")
public class CdrKafkaHandler {

    private final CdrTransactionFacade cdrTransactionFacade;

    @KafkaHandler(isDefault = true)
    public void cdrListener(CdrDto cdrDto) {
        log.info("Reading cdr {} with time {}", cdrDto, TimeUtils.toLocalDateTime(cdrDto.dateStart()));

        cdrTransactionFacade.handleCallTransaction(cdrDto);
    }
}
