package org.burgas.identityserver.mapper;

import org.burgas.identityserver.dto.AuthorityResponse;
import org.burgas.identityserver.entity.Authority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthorityMapper {

    public Mono<AuthorityResponse> toAuthorityResponse(Mono<Authority> authorityMono) {
        return authorityMono
                .flatMap(
                        authority -> Mono.fromCallable(
                                () -> AuthorityResponse.builder()
                                        .id(authority.getId())
                                        .name(authority.getName())
                                        .build()
                        )
                );
    }
}
