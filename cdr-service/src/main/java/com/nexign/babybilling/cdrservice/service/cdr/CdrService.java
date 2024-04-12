package com.nexign.babybilling.cdrservice.service.cdr;

import com.nexign.babybilling.cdrservice.config.property.KafkaProducerProperty;
import com.nexign.babybilling.cdrservice.mapper.CdrMapper;
import com.nexign.babybilling.payload.dto.CdrDto;
import com.nexign.babybilling.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CdrService {

    public static final String CDR_DIR = "cdr";
    
    private final CdrMapper cdrMapper;
    private final KafkaProducerProperty property;
    private final KafkaTemplate<Long, String> kafkaTemplate;

    /**
     * Создание cdr файла на основе переданных cdr записей
     * @param list
     * @return
     */
    public Path createCdrFile(List<CdrDto> list) {
        //создаем новый файл
        Path path = Paths.get(String.format("%s/%s", CDR_DIR, UUID.randomUUID()));
        FileUtils.createParentDir(path);

        //преобразовываем cdr в строковую запись
        String data = list.stream().map(cdrMapper::map)
                .collect(Collectors.joining("\n"));

        FileUtils.writeData(path, data);

        return path;
    }

    /**
     * Отправка файла через кафку
     * @param file
     */
    public void sendFile(Path file) {
        try {
            //преобразование файла в base64
            String encodedFile = Base64.getEncoder().encodeToString(Files.readAllBytes(file));

            SendResult<Long, String> result = kafkaTemplate.send(property.cdrFileTopic, System.currentTimeMillis(), encodedFile).get();

            var record = result.getRecordMetadata();
            log.info("Successfully sent file to {}-{}", record.topic(), record.partition());
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
