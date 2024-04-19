package com.nexign.babybilling.crmservice.service;

import com.nexign.babybilling.crmservice.config.property.ProducerProperty;
import com.nexign.babybilling.crmservice.payload.request.ChangeTariffRequest;
import com.nexign.babybilling.crmservice.payload.request.CreateCustomerRequest;
import com.nexign.babybilling.payload.dto.CustomerDto;
import com.nexign.babybilling.payload.events.ChangeTariffEvent;
import com.nexign.babybilling.payload.events.CreateNewCustomerEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final CustomerService customerService;
    private final ProducerProperty producerProperty;

    /**
     * Отправляем ивент, что мы создали нового пользователя
     * @param request запрос на создание пользователя
     */
    public void createCustomer(CreateCustomerRequest request) {
        CustomerDto customer = customerService.findByMsisnd(request.msisnd());
        if(!ObjectUtils.isEmpty(customer)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Такой абонент уже существует");

        try {
            SendResult<String, Object> res = kafkaTemplate.send(producerProperty.crmTopicName, request.msisnd(),
                    CreateNewCustomerEvent.builder()
                            .msisnd(request.msisnd())
                            .tariff(request.tariff())
                            .balance(request.balance() == null ? 100 : request.balance())
                            .build()).get();
            log.info("Successfully sent CreateCustomerEvent: {}-{}", res.getRecordMetadata().topic(), res.getRecordMetadata().partition());
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error while sending CreateCustomerEvent", e);
        }
    }

    public void changeTariff(ChangeTariffRequest request) {
        //todo cache and check exists user
        try {
            SendResult<String, Object> res = kafkaTemplate.send(producerProperty.crmTopicName, request.msisnd(),
                    ChangeTariffEvent.builder()
                            .msisnd(request.msisnd())
                            .tariff(request.tariff())
                            .build()).get();
            log.info("Successfully sent ChangeTariffEvent: {}-{}", res.getRecordMetadata().topic(), res.getRecordMetadata().partition());
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error while sending ChangeTariffEvent", e);
        }
    }
}
