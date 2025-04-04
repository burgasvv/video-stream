package org.burgas.backendserver.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Payload;

@MessagingGateway
public interface MessageGateway {

    @Gateway(requestChannel = "message-send-channel")
    String sendMessage(@Payload final String message);
}
