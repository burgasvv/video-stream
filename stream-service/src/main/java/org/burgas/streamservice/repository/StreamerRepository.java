package org.burgas.streamservice.repository;

import org.burgas.streamservice.entity.Streamer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StreamerRepository extends JpaRepository<Streamer, Long> {

    @Modifying
    @Query(
            nativeQuery = true,
            value = """
                    update identity set authority_id = :authorityId where id = :identityId
                    """
    )
    void updateIdentitySetIdentityAuthorityId(Long identityId, Long authorityId);
}
