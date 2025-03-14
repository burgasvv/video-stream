package org.burgas.identityserver.mapper;

import org.burgas.identityserver.dto.IdentityRequest;
import org.burgas.identityserver.dto.IdentityResponse;
import org.burgas.identityserver.entity.Identity;
import org.burgas.identityserver.repository.AuthorityRepository;
import org.burgas.identityserver.repository.IdentityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

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

    public Mono<Identity> toIdentity(Mono<IdentityRequest> identityRequestMono) {
        return identityRequestMono
                .flatMap(
                        identityRequest -> {
                            Long identityId = getData(identityRequest.id(), 0L);
                            return identityRepository
                                    .findById(identityId)
                                    .flatMap(
                                            identity -> Mono.fromCallable(
                                                    () -> {
                                                        String password = identityRequest.password() == null ? "" : identityRequest.password();
                                                        return Identity.builder()
                                                                .id(identity.getId())
                                                                .nickname(getData(identityRequest.nickname(), identity.getNickname()))
                                                                .password(getData(passwordEncoder.encode(password), identity.getPassword()))
                                                                .email(getData(identityRequest.email(), identity.getEmail()))
                                                                .authorityId(getData(identityRequest.authorityId(), identity.getAuthorityId()))
                                                                .enabled(getData(identityRequest.enabled(), identity.getEnabled()))
                                                                .isNew(false)
                                                                .build();
                                                    }
                                            )
                                    )
                                    .switchIfEmpty(
                                            Mono.fromCallable(
                                                    () -> Identity.builder()
                                                            .nickname(identityRequest.nickname())
                                                            .password(passwordEncoder.encode(identityRequest.password()))
                                                            .email(identityRequest.email())
                                                            .authorityId(identityRequest.authorityId())
                                                            .enabled(true)
                                                            .isNew(true)
                                                            .build()
                                            )
                                    );
                        }
                );
    }

    public Mono<IdentityResponse> toIdentityResponse(Mono<Identity> identityMono) {
        return identityMono
                .flatMap(
                        identity -> authorityRepository
                                .findById(identity.getAuthorityId())
                                .flatMap(
                                        authority -> authorityMapper
                                                .toAuthorityResponse(Mono.fromCallable(() -> authority))
                                                .flatMap(
                                                        authorityResponse ->
                                                                Mono.fromCallable(
                                                                        () -> IdentityResponse.builder()
                                                                                .id(identity.getId())
                                                                                .nickname(identity.getNickname())
                                                                                .password(identity.getPassword())
                                                                                .email(identity.getEmail())
                                                                                .enabled(identity.getEnabled())
                                                                                .authority(authorityResponse)
                                                                                .build()
                                                                )
                                                )
                                )

                );
    }
}
