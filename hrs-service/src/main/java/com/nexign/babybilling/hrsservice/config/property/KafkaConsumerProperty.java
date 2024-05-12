package com.nexign.babybilling.hrsservice.config.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerProperty {

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    public String bootstrapServers;
    @Value("${spring.kafka.topics.hrs-topic}")
    public String hrsTopic;
    @Value("${spring.kafka.consumer.group-id}")
    public String groupId;

    @Value("${spring.kafka.consumer.trust-packages}")
    public String trustedPackages;
}
