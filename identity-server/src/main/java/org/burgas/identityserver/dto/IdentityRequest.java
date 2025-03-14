package org.burgas.identityserver.dto;

public record IdentityRequest(
        Long id,
        String nickname,
        String password,
        String email,
        Boolean enabled,
        Long authorityId
) {
}
