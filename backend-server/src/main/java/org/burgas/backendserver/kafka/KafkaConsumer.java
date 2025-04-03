package org.burgas.backendserver.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.burgas.backendserver.dto.InvitationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public final class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(groupId = "consumer-config-group-id", topics = "streamer-invitation-response-topic")
    public void kafkaListenerInvitationResponse(final ConsumerRecord<String, InvitationResponse> consumerRecord) {
        log.info("Getting invitation response from kafka listener: {}", consumerRecord.value());
    }
}
