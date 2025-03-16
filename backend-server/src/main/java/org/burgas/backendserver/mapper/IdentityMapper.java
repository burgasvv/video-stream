package org.burgas.backendserver.mapper;

import org.burgas.backendserver.dto.AuthorityResponse;
import org.burgas.backendserver.dto.IdentityRequest;
import org.burgas.backendserver.dto.IdentityResponse;
import org.burgas.backendserver.entity.Identity;
import org.burgas.backendserver.repository.AuthorityRepository;
import org.burgas.backendserver.repository.IdentityRepository;
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
        Long identityId = getData(identityRequest.getId(), 0L);
        return identityRepository
                .findById(identityId)
                .map(
                        identity -> {
                            String password = identityRequest.getPassword() == null ? "" : identityRequest.getPassword();
                            return Identity.builder()
                                    .id(identity.getId())
                                    .nickname(getData(identityRequest.getNickname(), identity.getNickname()))
                                    .password(getData(passwordEncoder.encode(password), identity.getPassword()))
                                    .email(getData(identityRequest.getEmail(), identity.getEmail()))
                                    .authorityId(getData(identityRequest.getAuthorityId(), identity.getAuthorityId()))
                                    .enabled(getData(identityRequest.getEnabled(), identity.getEnabled()))
                                    .build();
                        }
                )
                .orElseGet(
                        () -> Identity.builder()
                                .nickname(identityRequest.getNickname())
                                .password(passwordEncoder.encode(identityRequest.getPassword()))
                                .email(identityRequest.getEmail())
                                .authorityId(2L)
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
