package org.burgas.backendserver.repository;

import org.burgas.backendserver.entity.Streamer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StreamerRepository extends JpaRepository<Streamer, Long> {

    @Query(
            nativeQuery = true,
            value = """
                    select s.* from streamer s join follow fu on s.id = fu.streamer_id
                                        where fu.follower_id = :followerId
                    """
    )
    List<Streamer> findStreamersByFollowerId(Long followerId);

    @Query(
            nativeQuery = true,
            value = """
                    select s.* from streamer s join subscription s2 on s.id = s2.streamer_id
                                        where s2.subscriber_id = :subscriberId
                    """
    )
    List<Streamer> findStreamersBySubscriberId(Long subscriberId);
}
