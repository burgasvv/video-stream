package org.burgas.backendserver.config;

import org.apache.kafka.common.serialization.StringSerializer;
import org.burgas.backendserver.dto.InvitationResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public Map<String, Object> producerConfig() {
        return Map.of(
                BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );
    }

    @Bean
    public ProducerFactory<String, InvitationResponse> invitationResponseProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<String, InvitationResponse> invitationResponseKafkaTemplate() {
        return new KafkaTemplate<>(invitationResponseProducerFactory());
    }
}
