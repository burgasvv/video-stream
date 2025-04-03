package org.burgas.backendserver.kafka;

import org.burgas.backendserver.dto.InvitationAnswer;
import org.burgas.backendserver.dto.InvitationResponse;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public final class KafkaProducer {

    private final KafkaTemplate<String, InvitationResponse> invitationResponseKafkaTemplate;

    public KafkaProducer(
            KafkaTemplate<String, InvitationResponse> invitationResponseKafkaTemplate
    ) {
        this.invitationResponseKafkaTemplate = invitationResponseKafkaTemplate;
    }

    public void sendInvitationResponse(final InvitationResponse invitationResponse) {
        this.invitationResponseKafkaTemplate
                .send("streamer-invitation-response-topic", invitationResponse);
    }
}
