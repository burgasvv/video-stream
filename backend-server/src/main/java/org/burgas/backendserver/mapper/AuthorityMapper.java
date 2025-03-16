package org.burgas.backendserver.mapper;

import org.burgas.backendserver.dto.AuthorityResponse;
import org.burgas.backendserver.entity.Authority;
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
