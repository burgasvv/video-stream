package org.burgas.backendserver.repository;

import org.burgas.backendserver.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findSubscriptionByStreamerIdAndSubscriberId(Long streamerId, Long subscriberId);

    List<Subscription> findSubscriptionsBySubscriberId(Long subscriberId);

    List<Subscription> findSubscriptionsByStreamerId(Long streamerId);

    void deleteSubscriptionByStreamerIdAndSubscriberId(Long streamerId, Long subscriberId);
}
