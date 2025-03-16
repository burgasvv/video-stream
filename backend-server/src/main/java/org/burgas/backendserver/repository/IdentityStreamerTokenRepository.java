package org.burgas.backendserver.repository;

import org.burgas.backendserver.entity.IdentityStreamerToken;
import org.burgas.backendserver.entity.IdentityStreamerTokenPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdentityStreamerTokenRepository
        extends JpaRepository<IdentityStreamerToken, IdentityStreamerTokenPK> {

    Optional<IdentityStreamerToken> findIdentityStreamerTokenByIdentityIdAndStreamerId(Long identityId, Long streamerId);
}
