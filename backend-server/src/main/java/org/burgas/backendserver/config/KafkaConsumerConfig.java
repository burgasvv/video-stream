package org.burgas.backendserver.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.burgas.backendserver.dto.InvitationResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.springframework.kafka.support.serializer.JsonDeserializer.TYPE_MAPPINGS;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public Map<String, Object> consumerConfig() {
        return Map.of(
                BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                GROUP_ID_CONFIG, "consumer-config-group-id",
                KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                TYPE_MAPPINGS, "org.burgas.backendserver.dto.InvitationResponse:org.burgas.backendserver.dto.InvitationResponse"
        );
    }

    @Bean
    public ConsumerFactory<String, InvitationResponse> invitationResponseConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, InvitationResponse> invitationResponseKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, InvitationResponse> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(invitationResponseConsumerFactory());
        return factory;
    }
}
