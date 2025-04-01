package org.burgas.backendserver.repository;

import org.burgas.backendserver.entity.Follow;
import org.burgas.backendserver.entity.FollowPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, FollowPK> {

    List<Follow> findFollowsByStreamerId(Long streamerId);

    List<Follow> findFollowsByFollowerId(Long followerId);

    Boolean existsByStreamerIdAndFollowerId(Long streamerId, Long followerId);

    void deleteFollowUpByStreamerIdAndFollowerId(Long streamerId, Long followerId);
}
