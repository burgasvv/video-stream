package org.burgas.backendserver.mapper;

import org.burgas.backendserver.dto.SubscriptionRequest;
import org.burgas.backendserver.dto.SubscriptionResponse;
import org.burgas.backendserver.entity.Identity;
import org.burgas.backendserver.entity.Streamer;
import org.burgas.backendserver.entity.Subscription;
import org.burgas.backendserver.entity.Tariff;
import org.burgas.backendserver.repository.IdentityRepository;
import org.burgas.backendserver.repository.StreamerRepository;
import org.burgas.backendserver.repository.SubscriptionRepository;
import org.burgas.backendserver.repository.TariffRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;

@Component
public final class SubscriptionMapper {

    private final SubscriptionRepository subscriptionRepository;
    private final StreamerRepository streamerRepository;
    private final StreamerMapper streamerMapper;
    private final IdentityRepository identityRepository;
    private final IdentityMapper identityMapper;
    private final TariffRepository tariffRepository;

    public SubscriptionMapper(
            SubscriptionRepository subscriptionRepository, StreamerRepository streamerRepository,
            StreamerMapper streamerMapper, IdentityRepository identityRepository,
            IdentityMapper identityMapper, TariffRepository tariffRepository
    ) {
        this.subscriptionRepository = subscriptionRepository;
        this.streamerRepository = streamerRepository;
        this.streamerMapper = streamerMapper;
        this.identityRepository = identityRepository;
        this.identityMapper = identityMapper;
        this.tariffRepository = tariffRepository;
    }

    private <T> T getData(T first, T second) {
        return first == null || first == "" ? second : first;
    }

    public Subscription toSubscription(final SubscriptionRequest subscriptionRequest) {
        return this.subscriptionRepository
                .findSubscriptionByStreamerIdAndSubscriberId(
                        subscriptionRequest.getStreamerId(), subscriptionRequest.getSubscriberId()
                )
                .map(
                        subscription -> Subscription.builder()
                                .streamerId(getData(subscriptionRequest.getStreamerId(), subscription.getStreamerId()))
                                .subscriberId(getData(subscriptionRequest.getSubscriberId(), subscription.getSubscriberId()))
                                .tariffId(getData(subscriptionRequest.getTariffId(), subscription.getTariffId()))
                                .subscribedAt(subscription.getSubscribedAt())
                                .build()
                )
                .orElseGet(
                        () -> Subscription.builder()
                                .streamerId(subscriptionRequest.getStreamerId())
                                .subscriberId(subscriptionRequest.getSubscriberId())
                                .tariffId(subscriptionRequest.getTariffId())
                                .subscribedAt(now())
                                .build()
                );
    }

    public SubscriptionResponse toSubscriptionResponse(final Subscription subscription) {
        return SubscriptionResponse.builder()
                .streamer(
                        this.streamerMapper.toStreamerResponse(
                                this.streamerRepository
                                        .findById(subscription.getStreamerId())
                                        .orElseGet(Streamer::new)
                        )
                )
                .subscriber(
                        this.identityMapper.toIdentityResponse(
                                this.identityRepository
                                        .findById(subscription.getSubscriberId())
                                        .orElseGet(Identity::new)
                        )
                )
                .tariff(this.tariffRepository.findById(subscription.getTariffId()).orElseGet(Tariff::new))
                .subscribedAt(subscription.getSubscribedAt().format(ofPattern("dd.MM.yyyy, hh:mm:ss")))
                .build();
    }
}
