package org.burgas.identityserver.service;

import org.burgas.identityserver.mapper.IdentityMapper;
import org.burgas.identityserver.repository.IdentityRepository;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomUserDetailsService implements ReactiveUserDetailsService {

    private final IdentityRepository identityRepository;
    private final IdentityMapper identityMapper;

    public CustomUserDetailsService(IdentityRepository identityRepository, IdentityMapper identityMapper) {
        this.identityRepository = identityRepository;
        this.identityMapper = identityMapper;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return identityRepository
                .findIdentityByEmail(username)
                .flatMap(identity -> identityMapper
                        .toIdentityResponse(Mono.fromCallable(() -> identity))
                );
    }
}
