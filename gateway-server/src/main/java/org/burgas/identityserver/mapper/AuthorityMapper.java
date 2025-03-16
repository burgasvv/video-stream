package org.burgas.identityserver.mapper;

import org.burgas.identityserver.dto.AuthorityResponse;
import org.burgas.identityserver.entity.Authority;
import org.springframework.stereotype.Component;

@Component
public class AuthorityMapper {

    public AuthorityResponse toAuthorityResponse(Authority authority) {
        return AuthorityResponse.builder()
                .id(authority.getId())
                .name(authority.getName())
                .build();
    }
}
