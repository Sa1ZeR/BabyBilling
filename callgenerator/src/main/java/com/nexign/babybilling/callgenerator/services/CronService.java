package com.nexign.babybilling.callgenerator.services;

import com.nexign.babybilling.payload.dto.CdrDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor
public class CronService {

    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private final KafkaTemplate<Long, CdrDto> kafkaTemplate;
    private final CdrGeneratorService cdrGeneratorService;
    private final Random random;

    @Value("${spring.kafka.producer.cdr-generator-topic-name}")
    private String cdrTopic;

    AtomicReference<LocalDateTime> timeForBabyBilling = new AtomicReference<>();

    /**
     * Данный метод генерирует cdr записи и отправляет их в шину
     */
    @Scheduled(fixedDelay = 10L)
    public void generateCdr() {
        if(timeForBabyBilling.get().getYear() >= 2025) {
            log.info("Наступил {} год. Запустите тарификацию еще раз", timeForBabyBilling.get().getYear());
            return;
        }

        timeForBabyBilling.set(timeForBabyBilling.get().plusSeconds(random.nextInt(3800))); //шаг звонков
        LocalDateTime curDate = timeForBabyBilling.get();
        log.info("Сейчас {}.{}.{}", curDate.getYear(), curDate.getMonth().getValue(), curDate.getDayOfMonth());

        //тут определяется сколько пользователей могут зводить в один промежуток времени (с небольшой погрешностью)
        for (int i = 0; i < random.nextInt(15) + 1; i++) {
            executorService.submit(() ->  {
                //создание cdr
                Optional<CdrDto> cdrDto = cdrGeneratorService.generateCdrRecord(timeForBabyBilling.get());

                if(cdrDto.isEmpty()) return;
                //отправляем в шину
                try {
                    CompletableFuture<SendResult<Long, CdrDto>> send = kafkaTemplate.send(cdrTopic, cdrDto.get().dateStart(), cdrDto.get());
                    RecordMetadata recordMetadata = send.get().getRecordMetadata();
                    log.info("Successfully write {} to {}-{}", cdrDto.get(), recordMetadata.topic(), recordMetadata.partition());
                } catch (InterruptedException | ExecutionException e) {
                    log.error("Error while generating cdr data", e);
                }
            });
            timeForBabyBilling.set(timeForBabyBilling.get().plus(random.nextInt(random.nextInt(50) + 1), ChronoUnit.MILLIS)); //вычисление погрешности
        }
    }

    @PostConstruct
    public void onInit() {
        timeForBabyBilling.set(LocalDateTime.of(2024,1, 1, 0, 0, 0));
    }
}
