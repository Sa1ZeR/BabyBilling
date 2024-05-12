package com.nexign.babybilling.brtservice.config;

import com.nexign.babybilling.brtservice.config.property.KafkaConsumerProperty;
import com.nexign.babybilling.brtservice.config.property.KafkaProducerProperty;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    private final KafkaConsumerProperty consumerProperty;
    private final KafkaProducerProperty kafkaProducerProperty;

    @Bean
    public NewTopic cdrPlusTopic() {
        return TopicBuilder.name(kafkaProducerProperty.hrsTopic)
                .partitions(1)
                .replicas(Integer.parseInt(kafkaProducerProperty.syncReplicas))
                .configs(Map.of("min.insync.replicas", String.valueOf(Math.max(Integer.parseInt(kafkaProducerProperty.syncReplicas)-1, 1)))) //число реплик, которые должны быть синхронизированы
                .build();
    }

    @Bean
    public <K, V> ProducerFactory<K, V> producerFactory() {
        Map<String, Object> map = new HashMap<>();
;
        map.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerProperty.bootstrapServers);
        map.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProducerProperty.deserializationKeyClass);
        map.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProducerProperty.deserializationValueClass);
        map.put(ProducerConfig.ACKS_CONFIG, kafkaProducerProperty.acks);

        return new DefaultKafkaProducerFactory<>(map);
    }

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

    @Bean
    public ConsumerFactory<String, Object> consumerStringFactory() {
        Map<String, Object> map = new HashMap<>();

        map.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerProperty.bootstrapServers);
        map.put(ConsumerConfig.GROUP_ID_CONFIG, consumerProperty.groupId); //указывается, если будет запущено несколько инстансов. Партиции будут равномерно распределены
        map.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        map.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class); //если не найдет как сериализовать класс, то кинет ошибку и не будет пытаться повторно его десериализовать
        map.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(map);
    }

    @Bean
    public <K, V>ConcurrentKafkaListenerContainerFactory<K, V> concurrentKafkaListenerContainerFactory(
            ConsumerFactory<K, V> consumerStringFactory) {
        ConcurrentKafkaListenerContainerFactory<K, V> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerStringFactory);

        return factory;
    }

    @Bean
    public ConsumerFactory<String, Object> consumerJsonFactory() {
        Map<String, Object> map = new HashMap<>();

        map.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerProperty.bootstrapServers);
        map.put(ConsumerConfig.GROUP_ID_CONFIG, "json"); //указывается, если будет запущено несколько инстансов. Партиции будут равномерно распределены
        map.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        map.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class); //если не найдет как сериализовать класс, то кинет ошибку и не будет пытаться повторно его десериализовать
        map.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        map.put(JsonDeserializer.TRUSTED_PACKAGES, consumerProperty.trustedPackages); //небезопасно, надо указывать явно путь до наших классов =)

        return new DefaultKafkaConsumerFactory<>(map);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> concurrentKafkaJsonListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerJsonFactory());

        return factory;
    }
}
