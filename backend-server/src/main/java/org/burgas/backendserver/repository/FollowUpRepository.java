package org.burgas.backendserver.repository;

import org.burgas.backendserver.entity.FollowUp;
import org.burgas.backendserver.entity.FollowUpPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowUpRepository extends JpaRepository<FollowUp, FollowUpPK> {

    List<FollowUp> findFollowUpsByStreamerId(Long streamerId);

    List<FollowUp> findFollowUpsByFollowerId(Long followerId);

    Boolean existsByStreamerIdAndFollowerId(Long streamerId, Long followerId);

    void deleteFollowUpByStreamerIdAndFollowerId(Long streamerId, Long followerId);
}
