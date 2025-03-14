package org.burgas.identityserver.mapper;

import org.burgas.identityserver.dto.AuthorityResponse;
import org.burgas.identityserver.dto.IdentityRequest;
import org.burgas.identityserver.dto.IdentityResponse;
import org.burgas.identityserver.entity.Identity;
import org.burgas.identityserver.repository.AuthorityRepository;
import org.burgas.identityserver.repository.IdentityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class IdentityMapper {

    private final IdentityRepository identityRepository;
    private final AuthorityRepository authorityRepository;
    private final AuthorityMapper authorityMapper;
    private final PasswordEncoder passwordEncoder;

    public IdentityMapper(
            IdentityRepository identityRepository, AuthorityRepository authorityRepository,
            AuthorityMapper authorityMapper, PasswordEncoder passwordEncoder
    ) {
        this.identityRepository = identityRepository;
        this.authorityRepository = authorityRepository;
        this.authorityMapper = authorityMapper;
        this.passwordEncoder = passwordEncoder;
    }

    private <T> T getData(T first, T second) {
        return first == null || first == "" ? second : first;
    }

    public Identity toIdentity(IdentityRequest identityRequest) {
        Long identityId = getData(identityRequest.id(), 0L);
        return identityRepository
                .findById(identityId)
                .map(
                        identity -> {
                            String password = identityRequest.password() == null ? "" : identityRequest.password();
                            return Identity.builder()
                                    .id(identity.getId())
                                    .nickname(getData(identityRequest.nickname(), identity.getNickname()))
                                    .password(getData(passwordEncoder.encode(password), identity.getPassword()))
                                    .email(getData(identityRequest.email(), identity.getEmail()))
                                    .authorityId(getData(identityRequest.authorityId(), identity.getAuthorityId()))
                                    .enabled(getData(identityRequest.enabled(), identity.getEnabled()))
                                    .build();
                        }
                )
                .orElseGet(
                        () -> Identity.builder()
                                .nickname(identityRequest.nickname())
                                .password(passwordEncoder.encode(identityRequest.password()))
                                .email(identityRequest.email())
                                .authorityId(identityRequest.authorityId())
                                .enabled(true)
                                .build()
                );
    }

    public IdentityResponse toIdentityResponse(Identity identity) {
        return IdentityResponse.builder()
                .id(identity.getId())
                .nickname(identity.getNickname())
                .password(identity.getPassword())
                .email(identity.getEmail())
                .enabled(identity.getEnabled())
                .authority(
                        authorityRepository
                                .findById(identity.getAuthorityId())
                                .map(authorityMapper::toAuthorityResponse)
                                .orElseGet(AuthorityResponse::new)
                )
                .build();
    }
}
