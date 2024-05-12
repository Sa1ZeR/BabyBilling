package com.nexign.babybilling.brtservice.handler;

import com.nexign.babybilling.brtservice.facade.CustomerFacade;
import com.nexign.babybilling.payload.events.ChangeTariffEvent;
import com.nexign.babybilling.payload.events.CreateNewCustomerEvent;
import com.nexign.babybilling.payload.events.CustomerPaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(topics = {"${spring.kafka.topics.crm-event-topic}"}, containerFactory = "concurrentKafkaJsonListenerContainerFactory")
public class CrmEventHandler {

    private final CustomerFacade customerFacade;

    /**
     * Слушает ивент на создание нового абонента
     * @param event {@link CreateNewCustomerEvent}
     */
    @KafkaHandler(isDefault = true)
    public void handleCreateCustomer(CreateNewCustomerEvent event) {
        log.info("Successfully received CreateCustomerEvent {}", event);
        customerFacade.createNewCustomer(event);
    }

    /**
     * Слушает ивент на изменение тарифа
     * @param event {@link ChangeTariffEvent}
     */
    @KafkaHandler()
    public void handleChangeTariff(ChangeTariffEvent event) {
        log.info("Successfully received CreateCustomerEvent {}", event);
        customerFacade.changeTariff(event);
    }

    /**
     * Слушает ивент на изменение баланса
     * @param event {@link CustomerPaymentEvent}
     */
    @KafkaHandler()
    public void handlePayment(CustomerPaymentEvent event) {
        log.info("Successfully received CustomerPaymentEvent {}", event);
        customerFacade.payment(event);
    }
}
