package com.nexign.babybilling.callgenerator.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.producer.cdr-generator-topic-name}")
    private String cdrTopic;

    @Bean
    public NewTopic createCdrTopic() {
        return TopicBuilder.name(cdrTopic)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replaces", "2")) //число реплик, которые должны быть синхронизированы
                .build();
    }
}
