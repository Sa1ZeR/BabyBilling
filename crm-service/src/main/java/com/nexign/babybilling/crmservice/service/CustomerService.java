package com.nexign.babybilling.crmservice.service;

import com.nexign.babybilling.crmservice.config.property.ProducerProperty;
import com.nexign.babybilling.crmservice.payload.request.PaymentRequest;
import com.nexign.babybilling.domain.Constants;
import com.nexign.babybilling.payload.dto.CustomerDto;
import com.nexign.babybilling.payload.events.ChangeTariffEvent;
import com.nexign.babybilling.payload.events.CustomerPaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ProducerProperty producerProperty;
    private final RestTemplate restTemplate;

    /**
     * Получение абонента по номеру телефона
     * @param msisnd номер абонента
     * @return dto абонента {@link CustomerDto}
     */
    @Cacheable(cacheNames = "customerCache", key = "#msisnd", unless = "#result == null")
    public CustomerDto findByMsisnd(String msisnd) {
        log.info("Try to getting customer {}...", msisnd);
        try {
            ResponseEntity<CustomerDto> response = restTemplate.exchange(String.format(Constants.BRT_SERVICE_URL + "/api/customer/%s", msisnd),
                    HttpMethod.GET, null, CustomerDto.class);
            if(response.getStatusCode().value() / 100 != 2) {
                log.error("Error while getting customer {}", response);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
            }
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        }
    }

    /**
     * поиск абонента по его номеру и проверка по паролю
     * @param msisnd номер абонента
     * @param password пароль в зашифрованном виде
     * @return dto абонента {@link CustomerDto}
     */
    public CustomerDto findByMsisndAndPassword(String msisnd, String password) {
        try {
            ResponseEntity<CustomerDto> response = restTemplate.exchange(String.format(Constants.BRT_SERVICE_URL + "/api/customer/?msisnd=%s&password=%s", msisnd, password),
                    HttpMethod.GET, null, CustomerDto.class);
            if(response.getStatusCode().value() / 100 != 2) {
                log.error("Error while getting customer {}", response);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
            }
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            log.error("Error while getting customer {}", e.getMessage());
            return null;
        }
    }

    /**
     * Отправка солбытия об изменении баланса
     * @param msisnd номер абонента
     * @param amount сумма пополнения
     */

    public void payment(String msisnd, double amount) {
        try {
            SendResult<String, Object> res = kafkaTemplate.send(producerProperty.crmTopicName, msisnd,
                    CustomerPaymentEvent.builder()
                            .msisnd(msisnd)
                            .amount(amount)
                            .build()).get();
            log.info("Successfully sent CustomerPaymentEvent: {}-{}", res.getRecordMetadata().topic(), res.getRecordMetadata().partition());
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error while sending CustomerPaymentEvent", e);
        }
    }

    public void changeTariff(String msisnd, String tariff) {
        try {
            SendResult<String, Object> res = kafkaTemplate.send(producerProperty.crmTopicName, msisnd,
                    ChangeTariffEvent.builder()
                            .msisnd(msisnd)
                            .tariff(tariff)
                            .build()).get();
            log.info("Successfully sent ChangeTariffEvent: {}-{}", res.getRecordMetadata().topic(), res.getRecordMetadata().partition());
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error while sending ChangeTariffEvent", e);
        }
    }
}
