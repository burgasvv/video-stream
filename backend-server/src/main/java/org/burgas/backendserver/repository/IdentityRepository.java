package org.burgas.backendserver.repository;

import org.burgas.backendserver.entity.Identity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IdentityRepository extends JpaRepository<Identity, Long> {

    Optional<Identity> findIdentityByEmail(String email);

    @Query(
            nativeQuery = true,
            value = """
                    select i.* from identity i join identity_streamer_token ist on i.id = ist.identity_id
                                        where ist.token = :token
                    """
    )
    Optional<Identity> findIdentityByIdentityStreamerToken(UUID token);
}
