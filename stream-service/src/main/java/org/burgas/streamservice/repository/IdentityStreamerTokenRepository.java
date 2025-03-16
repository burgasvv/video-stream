package org.burgas.streamservice.repository;

import org.burgas.streamservice.entity.IdentityStreamerToken;
import org.burgas.streamservice.entity.IdentityStreamerTokenPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdentityStreamerTokenRepository
        extends JpaRepository<IdentityStreamerToken, IdentityStreamerTokenPK> {

    Optional<IdentityStreamerToken> findIdentityStreamerTokenByIdentityIdAndStreamerId(Long identityId, Long streamerId);
}
