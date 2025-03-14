package org.burgas.identityserver.service;

import org.burgas.identityserver.dto.IdentityRequest;
import org.burgas.identityserver.dto.IdentityResponse;
import org.burgas.identityserver.mapper.IdentityMapper;
import org.burgas.identityserver.repository.IdentityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public IdentityResponse findById(Long identityId) {
        return this.identityRepository
                .findById(identityId)
                .map(identityMapper::toIdentityResponse)
                .orElseGet(IdentityResponse::new);

    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public IdentityResponse createOrUpdate(IdentityRequest identityRequest) {
        return identityMapper
                .toIdentityResponse(
                        identityRepository.save(identityMapper.toIdentity(identityRequest))
                );
    }
}
