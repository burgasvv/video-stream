package org.burgas.backendserver.service;

import org.burgas.backendserver.dto.SubscriptionRequest;
import org.burgas.backendserver.dto.SubscriptionResponse;
import org.burgas.backendserver.entity.Subscription;
import org.burgas.backendserver.exception.SubscriptionNotFoundException;
import org.burgas.backendserver.mapper.SubscriptionMapper;
import org.burgas.backendserver.repository.SubscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static java.lang.Thread.ofVirtual;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.burgas.backendserver.message.SubscriptionMessage.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class SubscriptionService {

    private static final Logger log = LoggerFactory.getLogger(SubscriptionService.class);
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, SubscriptionMapper subscriptionMapper) {
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionMapper = subscriptionMapper;
    }

    public List<SubscriptionResponse> findSubscriptionsBySubscriberId(final Long subscriberId) {
        return this.subscriptionRepository
                .findSubscriptionsBySubscriberId(subscriberId)
                .stream()
                .peek(subscription -> log.info("Find subscription by subscriberId: {}", subscription))
                .map(subscriptionMapper::toSubscriptionResponse)
                .toList();
    }

    public SseEmitter findSubscriptionsBySubscriberIdSse(final Long subscriberId) {
        SseEmitter sseEmitter = new SseEmitter();
        ofVirtual()
                .start(
                        () -> {
                            this.subscriptionRepository
                                    .findSubscriptionsBySubscriberId(subscriberId)
                                    .stream()
                                    .peek(subscription -> log.info("Find subscription by subscriberId by sse: {}", subscription))
                                    .map(subscriptionMapper::toSubscriptionResponse)
                                    .forEach(
                                            subscriptionResponse -> {
                                                try {
                                                    Set<ResponseBodyEmitter.DataWithMediaType> data = SseEmitter.event()
                                                            .data(subscriptionResponse, APPLICATION_JSON)
                                                            .build();
                                                    sseEmitter.send(data);
                                                    log.info("Subscription data was send by sse");
                                                    SECONDS.sleep(1);

                                                } catch (IOException | InterruptedException e) {
                                                    throw new RuntimeException(e);
                                                }
                                            }
                                    );
                            sseEmitter.complete();
                        }
                );
        return sseEmitter;
    }

    public StreamingResponseBody findSubscriptionsBySubscriberIdStream(final Long subscriberId) {
        return outputStream ->
                this.subscriptionRepository
                        .findSubscriptionsBySubscriberId(subscriberId)
                        .stream()
                        .peek(subscription -> log.info("Find subscription by subscriberId in stream: {}", subscription))
                        .map(subscriptionMapper::toSubscriptionResponse)
                        .forEach(
                                subscriptionResponse -> {
                                    try {
                                        outputStream.write((subscriptionResponse.toString() + "\n").getBytes(UTF_8));
                                        outputStream.flush();
                                        log.info("Subscription data was written in stream");
                                        SECONDS.sleep(1);

                                    } catch (IOException | InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                        );
    }

    public List<SubscriptionResponse> findSubscriptionsByStreamerId(final Long streamerId) {
        return this.subscriptionRepository
                .findSubscriptionsByStreamerId(streamerId)
                .stream()
                .peek(subscription -> log.info("Find subscription by streamerId: {}", subscription))
                .map(subscriptionMapper::toSubscriptionResponse)
                .toList();
    }

    public SseEmitter findSubscriptionsByStreamerIdSse(final Long streamerId) {
        SseEmitter sseEmitter = new SseEmitter();
        ofVirtual()
                .start(
                        () -> {
                            this.subscriptionRepository
                                    .findSubscriptionsByStreamerId(streamerId)
                                    .stream()
                                    .peek(subscription -> log.info("Find subscription by streamerId by sse: {}", subscription))
                                    .map(subscriptionMapper::toSubscriptionResponse)
                                    .forEach(
                                            subscriptionResponse -> {
                                                try {
                                                    Set<ResponseBodyEmitter.DataWithMediaType> data = SseEmitter.event()
                                                            .data(subscriptionResponse, APPLICATION_JSON)
                                                            .build();
                                                    sseEmitter.send(data);
                                                    log.info("Subscription data was written by sse");
                                                    SECONDS.sleep(1);

                                                } catch (IOException | InterruptedException e) {
                                                    throw new RuntimeException(e);
                                                }
                                            }
                                    );
                            sseEmitter.complete();
                        }
                );
        return sseEmitter;
    }

    public StreamingResponseBody findSubscriptionsByStreamerIdStream(final Long streamerId) {
        return outputStream ->
                this.subscriptionRepository
                        .findSubscriptionsByStreamerId(streamerId)
                        .stream()
                        .peek(subscription -> log.info("Find subscription by streamerId in stream: {}", subscription))
                        .map(subscriptionMapper::toSubscriptionResponse)
                        .forEach(
                                subscriptionResponse -> {
                                    try {
                                        outputStream.write((subscriptionResponse.toString() + "\n").getBytes(UTF_8));
                                        outputStream.flush();
                                        SECONDS.sleep(1);

                                    } catch (IOException | InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                        );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String subscribeOrUpdate(final SubscriptionRequest subscriptionRequest) {
        Subscription saved = this.subscriptionRepository.save(
                this.subscriptionMapper.toSubscription(subscriptionRequest)
        );
        log.info("Subscriber successfully subscribed/update on subscription: {}", saved);
        return SUBSCRIPTION_SAVED.getMessage();
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String unsubscribeAndDelete(final Long streamerId, final Long subscriberId) {
        return this.subscriptionRepository
                .findSubscriptionByStreamerIdAndSubscriberId(streamerId, subscriberId)
                .map(
                        subscription -> {
                            this.subscriptionRepository.deleteSubscriptionByStreamerIdAndSubscriberId(
                                    subscription.getStreamerId(), subscription.getSubscriberId()
                            );
                            log.info("Subscription successfully deleted");
                            return SUBSCRIPTION_DELETED.getMessage();
                        }
                )
                .orElseThrow(
                        () -> {
                            log.error("Subscription not found");
                            return new SubscriptionNotFoundException(SUBSCRIPTION_NOT_FOUND.getMessage());
                        }
                );
    }
}
