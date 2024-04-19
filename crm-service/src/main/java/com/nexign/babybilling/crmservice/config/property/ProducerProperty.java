package com.nexign.babybilling.crmservice.config.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProducerProperty {
    @Value("${spring.kafka.producer.crm-event-topic}")
    public String crmTopicName;

    @Value("${spring.kafka.producer.bootstrap-servers}")
    public String bootstrapServers;

    @Value("${spring.kafka.producer.acks}")
    public String acks;

    @Value("${spring.kafka.producer.key-serializer}")
    public String deserializationKeyClass;

    @Value("${spring.kafka.producer.value-serializer}")
    public String deserializationValueClass;

    @Value("${spring.kafka.producer.min-insync-replicas}")
    public String syncReplicas;
}
