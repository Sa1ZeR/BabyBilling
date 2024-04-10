package com.nexign.babybilling.callgenerator.services;

import com.nexign.babybilling.payload.dto.CdrDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class CronService {

    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private final KafkaTemplate<Long, CdrDto> kafkaTemplate;
    private final CdrGeneratorService cdrGeneratorService;
    private final Random random;

    @Value("${spring.kafka.producer.cdr-generator-topic-name}")
    private String cdrTopic;

    /**
     * Данный метод генерирует cdr записи и отправляет их в шину
     */
    @Scheduled(cron = "* * * * * *")
    public void generateCdr() {
        AtomicReference<LocalDateTime> now = new AtomicReference<>();
        now.set(LocalDateTime.now());

        //тут определяется сколько пользователей могут зводить в один промежуток времени (с небольшой погрешностью)
        for (int i = 0; i < random.nextInt(5) + 1; i++) {
            executorService.submit(() ->  {
                //создание cdr
                CdrDto cdrDto = cdrGeneratorService.generateCdrRecord(now.get());

                //отправляем в шину
                kafkaTemplate.send(cdrTopic, cdrDto.dateStart(), cdrDto);
            });
            now.set(now.get().plus(random.nextInt(50), ChronoUnit.MILLIS)); //вычисление погрешности
        }
    }
}
