package org.burgas.identityserver.service;

import org.burgas.identityserver.dto.IdentityRequest;
import org.burgas.identityserver.dto.IdentityResponse;
import org.burgas.identityserver.mapper.IdentityMapper;
import org.burgas.identityserver.repository.IdentityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

@Service
@Transactional(readOnly = true, propagation = REQUIRED)
public class IdentityService {

    private final IdentityRepository identityRepository;
    private final IdentityMapper identityMapper;

    public IdentityService(IdentityRepository identityRepository, IdentityMapper identityMapper) {
        this.identityRepository = identityRepository;
        this.identityMapper = identityMapper;
    }

    public Mono<IdentityResponse> findById(Long identityId) {
        return this.identityRepository
                .findById(identityId)
                .flatMap(identity -> identityMapper.toIdentityResponse(
                        Mono.fromCallable(() -> identity)
                ));
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<IdentityResponse> createOrUpdate(Mono<IdentityRequest> identityRequestMono) {
        return identityRequestMono
                .flatMap(
                        identityRequest -> identityMapper
                                .toIdentity(Mono.fromCallable(() -> identityRequest))
                                .flatMap(identityRepository::save)
                                .flatMap(identity -> identityMapper.toIdentityResponse(
                                        Mono.fromCallable(() -> identity)
                                ))
                );
    }
}
