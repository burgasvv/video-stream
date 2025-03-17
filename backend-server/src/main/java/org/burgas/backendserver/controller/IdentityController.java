package org.burgas.backendserver.controller;

import org.burgas.backendserver.dto.IdentityRequest;
import org.burgas.backendserver.dto.IdentityResponse;
import org.burgas.backendserver.service.IdentityService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping(value = "/identities")
@CrossOrigin(value = "http:/localhost:4200")
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

    @GetMapping(value = "/async")
    public @ResponseBody ResponseEntity<List<IdentityResponse>> getAllIdentitiesAsync()
            throws ExecutionException, InterruptedException {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.identityService.findAllAsync().get());
    }

    @GetMapping(value = "/by-id", produces = APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<IdentityResponse> getIdentityById(@RequestParam Long identityId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(identityService.findById(identityId));
    }

    @GetMapping(value = "/by-id/async")
    public @ResponseBody ResponseEntity<IdentityResponse> getIdentityByIdAsync(@RequestParam Long identityId)
            throws ExecutionException, InterruptedException {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.identityService.findByIdAsync(identityId).get());
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
