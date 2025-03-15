package org.burgas.identityserver.controller;

import org.burgas.identityserver.dto.IdentityPrincipal;
import org.burgas.identityserver.dto.IdentityResponse;
import org.burgas.identityserver.mapper.IdentityPrincipalMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/authentication")
public class AuthenticationController {

    private final IdentityPrincipalMapper identityPrincipalMapper;

    public AuthenticationController(IdentityPrincipalMapper identityPrincipalMapper) {
        this.identityPrincipalMapper = identityPrincipalMapper;
    }

    @GetMapping(value = "/principal")
    public @ResponseBody Mono<IdentityPrincipal> getIdentityPrincipal(Mono<Authentication> authenticationMono) {
        return authenticationMono
                .filter(authentication -> authentication != null && authentication.isAuthenticated())
                .flatMap(
                        authentication -> Mono.fromCallable(
                                () -> identityPrincipalMapper
                                        .toIdentityPrincipal((IdentityResponse) authentication.getPrincipal(), true)
                        )
                )
                .switchIfEmpty(
                        Mono.fromCallable(
                                () -> IdentityPrincipal.builder().authenticated(false).build()
                        )
                );
    }
}
