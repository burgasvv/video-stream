package org.burgas.backendserver.service;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unused")
@Service
public class MessageService {

    @ServiceActivator(inputChannel = "message-send-channel")
    public void readMessage(Message<String> stringMessage) {
        MessageChannel replyChannel = (MessageChannel) stringMessage.getHeaders().getReplyChannel();
        Message<String> newMessage = MessageBuilder
                .withPayload("New Message payload with: " + stringMessage.getPayload())
                .build();
        requireNonNull(replyChannel).send(newMessage);
    }
}
