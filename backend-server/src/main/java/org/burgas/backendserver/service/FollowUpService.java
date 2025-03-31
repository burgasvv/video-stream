package org.burgas.backendserver.service;

import org.burgas.backendserver.dto.FollowUpResponse;
import org.burgas.backendserver.entity.FollowUp;
import org.burgas.backendserver.exception.AlreadyFollowException;
import org.burgas.backendserver.exception.StillNotFollowException;
import org.burgas.backendserver.mapper.FollowUpMapper;
import org.burgas.backendserver.repository.FollowUpRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;

import static java.lang.Thread.ofVirtual;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.LocalDateTime.now;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.burgas.backendserver.message.FollowUpMessage.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class FollowUpService {

    private static final Logger log = LoggerFactory.getLogger(FollowUpService.class);
    private final FollowUpRepository followUpRepository;
    private final FollowUpMapper followUpMapper;

    public FollowUpService(FollowUpRepository followUpRepository, FollowUpMapper followUpMapper) {
        this.followUpRepository = followUpRepository;
        this.followUpMapper = followUpMapper;
    }

    public List<FollowUpResponse> findAllByStreamerId(final Long streamerId) {
        return this.followUpRepository
                .findFollowUpsByStreamerId(streamerId)
                .stream()
                .peek(followUp -> log.info("Find follower by streamer: {}", followUp))
                .map(followUpMapper::toFollowUpResponse)
                .toList();
    }

    public SseEmitter findAllByStreamerIdSse(final Long streamerId) {
        SseEmitter sseEmitter = new SseEmitter();
        ofVirtual()
                .start(
                        () -> {
                            this.followUpRepository
                                    .findFollowUpsByStreamerId(streamerId)
                                    .stream()
                                    .map(followUpMapper::toFollowUpResponse)
                                    .forEach(
                                            followUp -> {
                                                try {
                                                    log.info("Follow up was found by streamer: {}", followUp);

                                                    Set<ResponseBodyEmitter.DataWithMediaType> data = getDataWithMediaTypes(followUp);
                                                    sseEmitter.send(data);

                                                    log.info("Follow Up by streamer was send by emitter: {}", data);
                                                    SECONDS.sleep(1);

                                                } catch (InterruptedException | IOException e) {
                                                    throw new RuntimeException(e);
                                                }
                                            }
                                    );
                            sseEmitter.complete();
                        }
                );
        return sseEmitter;
    }

    public StreamingResponseBody findAllByStreamerIdStream(final Long streamerId) {
        return outputStream ->
                this.followUpRepository
                        .findFollowUpsByStreamerId(streamerId)
                        .forEach(
                                followUp -> {
                                    try {
                                        log.info("Follow up by streamer Id was found: {}", followUp);

                                        writeFollowUpStream(outputStream, followUp);

                                        log.info("Follow up by streamer Id was written");
                                        SECONDS.sleep(1);

                                    } catch (InterruptedException | IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                        );
    }

    public List<FollowUpResponse> findAllByFollowerId(final Long followerId) {
        return this.followUpRepository
                .findFollowUpsByFollowerId(followerId)
                .stream()
                .peek(followUp -> log.info("Find streamer by follower: {}", followUp))
                .map(followUpMapper::toFollowUpResponse)
                .toList();
    }

    public SseEmitter findAllByFollowerIdSse(final Long followerId) {
        SseEmitter sseEmitter = new SseEmitter();
        ofVirtual().start(
                () -> {
                    this.followUpRepository
                            .findFollowUpsByFollowerId(followerId)
                            .stream()
                            .map(followUpMapper::toFollowUpResponse)
                            .forEach(
                                    followUp -> {
                                        try {
                                            log.info("Follow up was found by follower: {}", followUp);

                                            Set<ResponseBodyEmitter.DataWithMediaType> data = getDataWithMediaTypes(followUp);
                                            sseEmitter.send(data);

                                            SECONDS.sleep(1);
                                            log.info("Follow Up by follower was send by emitter: {}", data);

                                        } catch (InterruptedException | IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                            );
                    sseEmitter.complete();
                }
        );
        return sseEmitter;
    }

    private static @NotNull Set<ResponseBodyEmitter.DataWithMediaType> getDataWithMediaTypes(FollowUpResponse followUp) {
        return SseEmitter.event()
                .comment("New Comment")
                .data(followUp, APPLICATION_JSON)
                .build();
    }

    public StreamingResponseBody findAllByFollowerIdStream(final Long followerId) {
        return outputStream ->
                this.followUpRepository
                        .findFollowUpsByFollowerId(followerId)
                        .forEach(
                                followUp -> {
                                    try {
                                        log.info("Find follow up by follower Id: {}", followUp);

                                        writeFollowUpStream(outputStream, followUp);

                                        log.info("Follow up by follower Id was written");
                                        SECONDS.sleep(1);

                                    } catch (IOException | InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }

                                }
                        );
    }

    private static void writeFollowUpStream(OutputStream outputStream, FollowUp followUp) throws IOException {
        outputStream.write((followUp.toString() + "\n").getBytes(UTF_8));
        outputStream.flush();
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String followOnStreamer(final Long streamerId, final Long followerId) {
        if (!this.followUpRepository.existsByStreamerIdAndFollowerId(streamerId, followerId)) {
            FollowUp saved = this.followUpRepository.save(
                    FollowUp.builder()
                            .streamerId(streamerId)
                            .followerId(followerId)
                            .followedAt(now())
                            .build()
            );
            log.info("Successfully followed: {}", saved);
            return SUCCESSFULLY_FOLLOW.getMessage();

        } else {
            log.error("Already followed on this streamer");
            throw new AlreadyFollowException(ALREADY_FOLLOW.getMessage());
        }
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String unfollowOnStreamer(final Long streamerId, final Long followerId) {
        if (this.followUpRepository.existsByStreamerIdAndFollowerId(streamerId, followerId)) {
            this.followUpRepository.deleteFollowUpByStreamerIdAndFollowerId(streamerId, followerId);
            log.info("FollowUp successfully deleted");
            return SUCCESSFULLY_UNFOLLOW.getMessage();

        } else {
            log.error("You need to follow the streamer before unfollow it");
            throw new StillNotFollowException(STILL_NOT_FOLLOW.getMessage());
        }
    }
}
