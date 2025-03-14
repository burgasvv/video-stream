package org.burgas.identityserver.controller;

import org.burgas.identityserver.dto.IdentityRequest;
import org.burgas.identityserver.dto.IdentityResponse;
import org.burgas.identityserver.service.IdentityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping(value = "/identities")
public class IdentityController {

    private final IdentityService identityService;

    public IdentityController(IdentityService identityService) {
        this.identityService = identityService;
    }

    @GetMapping(value = "/by-id", produces = APPLICATION_JSON_VALUE)
    public @ResponseBody Mono<IdentityResponse> getIdentityById(@RequestParam Long identityId) {
        return identityService.findById(identityId);
    }

    @PostMapping(value = "/create")
    public @ResponseBody Mono<IdentityResponse> createIdentity(@RequestBody IdentityRequest identityRequest) {
        return identityService.createOrUpdate(Mono.fromCallable(() -> identityRequest));
    }

    @PutMapping(value = "/update")
    public @ResponseBody Mono<IdentityResponse> updateIdentity(@RequestBody IdentityRequest identityRequest) {
        return identityService.createOrUpdate(Mono.fromCallable(() -> identityRequest));
    }
}
