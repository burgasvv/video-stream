package org.burgas.backendserver.controller;

import org.burgas.backendserver.dto.InvitationAnswer;
import org.burgas.backendserver.dto.InvitationRequest;
import org.burgas.backendserver.dto.InvitationResponse;
import org.burgas.backendserver.service.InvitationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;

@Controller
@RequestMapping(value = "/invitations")
public class InvitationController {

    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @GetMapping(value = "/all/by-sender")
    public @ResponseBody ResponseEntity<List<InvitationResponse>> getInvitationsBySender(@RequestParam Long senderId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.invitationService.findBySenderId(senderId));
    }

    @GetMapping(value = "/all/by-receiver")
    public @ResponseBody ResponseEntity<List<InvitationResponse>> getInvitationsByReceiver(@RequestParam Long receiverId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.invitationService.findByReceiverId(receiverId));
    }

    @PostMapping(value = "/send")
    public @ResponseBody ResponseEntity<String> sendInvitation(
            @RequestParam Long senderId, @RequestBody InvitationRequest invitationRequest
    ) {
        invitationRequest.setSenderId(senderId);
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.invitationService.sendInvitation(invitationRequest));
    }

    @PutMapping(value = "/answer")
    public @ResponseBody ResponseEntity<String> answerInvitation(
            @RequestParam Long invitedId, @RequestParam(required = false) UUID streamKey,
            @RequestBody InvitationAnswer invitationAnswer
    ) {
        invitationAnswer.setInvitedId(invitedId);
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.invitationService.acceptOrDeclineInvitation(invitationAnswer, streamKey));
    }
}
