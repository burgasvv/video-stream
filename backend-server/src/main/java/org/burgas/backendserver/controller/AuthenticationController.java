package org.burgas.backendserver.controller;

import org.burgas.backendserver.dto.IdentityPrincipal;
import org.burgas.backendserver.dto.IdentityResponse;
import org.burgas.backendserver.mapper.IdentityPrincipalMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Controller
@RequestMapping("/authentication")
public class AuthenticationController {

    private final IdentityPrincipalMapper identityPrincipalMapper;

    public AuthenticationController(IdentityPrincipalMapper identityPrincipalMapper) {
        this.identityPrincipalMapper = identityPrincipalMapper;
    }

    @GetMapping(value = "/principal")
    public @ResponseBody ResponseEntity<IdentityPrincipal> getIdentityPrincipal(Authentication authentication) {
        return ofNullable(authentication)
                .filter(Authentication::isAuthenticated)
                .map(
                        _ -> ResponseEntity
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(identityPrincipalMapper
                                                .toIdentityPrincipal(
                                                        (IdentityResponse) authentication.getPrincipal(),
                                                        true)
                                )
                )
                .orElseGet(
                        () -> ResponseEntity
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(IdentityPrincipal.builder().authenticated(false).build())
                );
    }
}
