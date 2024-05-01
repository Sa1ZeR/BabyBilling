package com.nexign.babybilling.brtservice.config.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducerProperty {

    @Value("${spring.kafka.topics.hrs-topic}")
    public String hrsTopic;

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
