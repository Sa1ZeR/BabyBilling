package com.nexign.babybilling.cdrservice.config.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerProperty {

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    public String bootstrapServers;
    @Value("${spring.kafka.consumer.cdr-generator-topic-name}")
    public String cdrGeneratorTopicName;
    @Value("${spring.kafka.consumer.group-id}")
    public String groupId;

    @Value("${spring.kafka.consumer.trust-packages}")
    public String trustedPackages;
}
