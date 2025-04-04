package org.burgas.backendserver.controller;

import org.burgas.backendserver.gateway.MessageGateway;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.TEXT_PLAIN;

@Controller
@RequestMapping(value = "/messages")
public class MessageController {

    private final MessageGateway messageGateway;

    public MessageController(MessageGateway messageGateway) {
        this.messageGateway = messageGateway;
    }

    @PostMapping(value = "/new-message")
    public @ResponseBody ResponseEntity<String> sendMessageController(@RequestParam String message) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.messageGateway.sendMessage(message));
    }
}
