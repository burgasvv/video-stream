package org.burgas.identityserver.controller;

import org.burgas.identityserver.dto.IdentityRequest;
import org.burgas.identityserver.dto.IdentityResponse;
import org.burgas.identityserver.service.IdentityService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping(value = "/identities")
public class IdentityController {

    private final IdentityService identityService;

    public IdentityController(IdentityService identityService) {
        this.identityService = identityService;
    }

    @GetMapping
    public @ResponseBody ResponseEntity<List<IdentityResponse>> getAllIdentities() {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(identityService.findAll());
    }

    @GetMapping(value = "/by-id", produces = APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<IdentityResponse> getIdentityById(@RequestParam Long identityId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(identityService.findById(identityId));
    }

    @GetMapping(value = "/by-identity-streamer-token/{token}")
    public @ResponseBody ResponseEntity<IdentityResponse> getIdentityByIdentityStreamerToken(@PathVariable UUID token) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(identityService.findByIdentityStreamerToken(token));
    }

    @PostMapping(value = "/create")
    public @ResponseBody ResponseEntity<IdentityResponse> createIdentity(@RequestBody IdentityRequest identityRequest) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(identityService.createOrUpdate(identityRequest));
    }

    @PutMapping(value = "/update")
    public @ResponseBody ResponseEntity<IdentityResponse> updateIdentity(
            @RequestBody IdentityRequest identityRequest, @RequestParam Long identityId
    ) {
        identityRequest.setId(identityId);
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(identityService.createOrUpdate(identityRequest));
    }
}
