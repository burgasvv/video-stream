package org.burgas.identityserver.mapper;

import org.burgas.identityserver.dto.IdentityPrincipal;
import org.burgas.identityserver.dto.IdentityResponse;
import org.springframework.stereotype.Component;

@Component
public class IdentityPrincipalMapper {

    public IdentityPrincipal toIdentityPrincipal(IdentityResponse identityResponse, Boolean authenticated) {
        return IdentityPrincipal.builder()
                .id(identityResponse.getId())
                .nickname(identityResponse.getNickname())
                .password(identityResponse.getPassword())
                .email(identityResponse.getEmail())
                .authority(identityResponse.getAuthority().getAuthority())
                .enabled(identityResponse.getEnabled())
                .authenticated(authenticated)
                .build();
    }
}
