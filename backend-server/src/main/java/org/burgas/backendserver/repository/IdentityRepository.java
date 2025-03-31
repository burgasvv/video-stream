package org.burgas.backendserver.repository;

import org.burgas.backendserver.entity.Identity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Query(
            nativeQuery = true,
            value = """
                    select i.* from identity i join follow_up fu on i.id = fu.follower_id
                                        where fu.streamer_id = :streamerId
                    """
    )
    List<Identity> findFollowersByStreamerId(Long streamerId);

    @Query(
            nativeQuery = true,
            value = """
                    select i.* from identity i join subscription s on i.id = s.subscriber_id
                                        where s.streamer_id = :streamerId
                    """
    )
    List<Identity> findSubscribersByStreamerId(Long streamerId);
}
