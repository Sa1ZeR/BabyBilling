package com.nexign.babybilling.brtservice.config;

import com.nexign.babybilling.brtservice.config.property.KafkaConsumerProperty;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    private final KafkaConsumerProperty consumerProperty;

    @Bean
    public ConsumerFactory<Long, Object> consumerFactory() {
        Map<String, Object> map = new HashMap<>();

        map.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerProperty.bootstrapServers);
        map.put(ConsumerConfig.GROUP_ID_CONFIG, consumerProperty.groupId); //указывается, если будет запущено несколько инстансов. Партиции будут равномерно распределены
        map.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        map.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class); //если не найдет как сериализовать класс, то кинет ошибку и не будет пытаться повторно его десериализовать
        map.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        map.put(JsonDeserializer.TRUSTED_PACKAGES, consumerProperty.trustedPackages); //небезопасно, надо указывать явно путь до наших классов =)

        return new DefaultKafkaConsumerFactory<>(map);
    }

    //бин для листнера
    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, Object> concurrentKafkaListenerContainerFactory(
            ConsumerFactory<Long, Object> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<Long, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        return factory;
    }
}
