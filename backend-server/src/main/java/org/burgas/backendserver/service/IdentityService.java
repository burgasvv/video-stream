package org.burgas.backendserver.service;

import org.burgas.backendserver.dto.IdentityRequest;
import org.burgas.backendserver.dto.IdentityResponse;
import org.burgas.backendserver.mapper.IdentityMapper;
import org.burgas.backendserver.repository.IdentityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<IdentityResponse> findAll() {
        return this.identityRepository
                .findAll()
                .stream()
                .map(identityMapper::toIdentityResponse)
                .toList();
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
