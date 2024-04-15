package com.nexign.babybilling.cdrservice.config;

import com.nexign.babybilling.cdrservice.config.property.KafkaConsumerProperty;
import com.nexign.babybilling.cdrservice.config.property.KafkaProducerProperty;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    private final KafkaConsumerProperty kafkaConsumerProperty;
    private final KafkaProducerProperty kafkaProducerProperty;

    @Bean
    public NewTopic cdrFileTopic() {
        return TopicBuilder.name(kafkaProducerProperty.cdrFileTopic)
                .partitions(1)
                .replicas(Integer.parseInt(kafkaProducerProperty.syncReplicas))
                .configs(Map.of("min.insync.replicas", String.valueOf(Math.max(Integer.parseInt(kafkaProducerProperty.syncReplicas)-1, 1)))) //число реплик, которые должны быть синхронизированы
                .build();
    }

    @Bean
    public <K, V> ProducerFactory<K, V> producerFactory() {
        Map<String, Object> map = new HashMap<>();

        map.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerProperty.bootstrapServers);
        map.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProducerProperty.deserializationKeyClass);
        map.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProducerProperty.deserializationValueClass);
        map.put(ProducerConfig.ACKS_CONFIG, kafkaProducerProperty.acks);

        return new DefaultKafkaProducerFactory<>(map);
    }

    @Bean
    public <K, V> KafkaTemplate<K, V> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConsumerFactory<Long, Object> consumerFactory() {
        Map<String, Object> map = new HashMap<>();

        map.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumerProperty.bootstrapServers);
        map.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerProperty.groupId); //указывается, если будет запущено несколько инстансов. Партиции будут равномерно распределены
        map.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        map.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class); //если не найдет как сериализовать класс, то кинет ошибку и не будет пытаться повторно его десериализовать
        map.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        map.put(JsonDeserializer.TRUSTED_PACKAGES, kafkaConsumerProperty.trustedPackages); //небезопасно, надо указывать явно путь до наших классов =)

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
