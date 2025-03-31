package org.burgas.backendserver.repository;

import org.burgas.backendserver.entity.Subscription;
import org.burgas.backendserver.entity.SubscriptionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionPK> {

    Optional<Subscription> findSubscriptionByStreamerIdAndSubscriberId(Long streamerId, Long subscriberId);
}
