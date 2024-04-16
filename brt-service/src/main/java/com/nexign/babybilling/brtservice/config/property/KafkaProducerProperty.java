package com.nexign.babybilling.brtservice.config.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducerProperty {

    @Value("${spring.kafka.topics.calculation-topic}")
    public String calcTopic;
}