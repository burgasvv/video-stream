package org.burgas.backendserver.repository;

import org.burgas.backendserver.entity.Identity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdentityRepository extends JpaRepository<Identity, Long> {

    Optional<Identity> findIdentityByEmail(String email);

    @Modifying
    @Query(
            nativeQuery = true,
            value = """
                    update identity set authority_id = :authorityId where id = :identityId
                    """
    )
    void updateIdentitySetIdentityAuthorityId(Long identityId, Long authorityId);
}
