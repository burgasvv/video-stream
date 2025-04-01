package org.burgas.backendserver.service;

import org.burgas.backendserver.dto.FollowResponse;
import org.burgas.backendserver.entity.Follow;
import org.burgas.backendserver.exception.AlreadyFollowException;
import org.burgas.backendserver.exception.StillNotFollowException;
import org.burgas.backendserver.mapper.FollowMapper;
import org.burgas.backendserver.repository.FollowRepository;
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
public class FollowService {

    private static final Logger log = LoggerFactory.getLogger(FollowService.class);
    private final FollowRepository followRepository;
    private final FollowMapper followMapper;

    public FollowService(FollowRepository followRepository, FollowMapper followMapper) {
        this.followRepository = followRepository;
        this.followMapper = followMapper;
    }

    public List<FollowResponse> findAllByStreamerId(final Long streamerId) {
        return this.followRepository
                .findFollowsByStreamerId(streamerId)
                .stream()
                .peek(follow -> log.info("Find follower by streamer: {}", follow))
                .map(followMapper::toFollowResponse)
                .toList();
    }

    public SseEmitter findAllByStreamerIdSse(final Long streamerId) {
        SseEmitter sseEmitter = new SseEmitter();
        ofVirtual()
                .start(
                        () -> {
                            this.followRepository
                                    .findFollowsByStreamerId(streamerId)
                                    .stream()
                                    .map(followMapper::toFollowResponse)
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
                this.followRepository
                        .findFollowsByStreamerId(streamerId)
                        .forEach(
                                follow -> {
                                    try {
                                        log.info("Follow up by streamer Id was found: {}", follow);

                                        writeFollowUpStream(outputStream, follow);

                                        log.info("Follow up by streamer Id was written");
                                        SECONDS.sleep(1);

                                    } catch (InterruptedException | IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                        );
    }

    public List<FollowResponse> findAllByFollowerId(final Long followerId) {
        return this.followRepository
                .findFollowsByFollowerId(followerId)
                .stream()
                .peek(follow -> log.info("Find streamer by follower: {}", follow))
                .map(followMapper::toFollowResponse)
                .toList();
    }

    public SseEmitter findAllByFollowerIdSse(final Long followerId) {
        SseEmitter sseEmitter = new SseEmitter();
        ofVirtual().start(
                () -> {
                    this.followRepository
                            .findFollowsByFollowerId(followerId)
                            .stream()
                            .map(followMapper::toFollowResponse)
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

    private static @NotNull Set<ResponseBodyEmitter.DataWithMediaType> getDataWithMediaTypes(FollowResponse followUp) {
        return SseEmitter.event()
                .comment("New Comment")
                .data(followUp, APPLICATION_JSON)
                .build();
    }

    public StreamingResponseBody findAllByFollowerIdStream(final Long followerId) {
        return outputStream ->
                this.followRepository
                        .findFollowsByFollowerId(followerId)
                        .forEach(
                                follow -> {
                                    try {
                                        log.info("Find follow up by follower Id: {}", follow);

                                        writeFollowUpStream(outputStream, follow);

                                        log.info("Follow up by follower Id was written");
                                        SECONDS.sleep(1);

                                    } catch (IOException | InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }

                                }
                        );
    }

    private static void writeFollowUpStream(OutputStream outputStream, Follow follow) throws IOException {
        outputStream.write((follow.toString() + "\n").getBytes(UTF_8));
        outputStream.flush();
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String followOnStreamer(final Long streamerId, final Long followerId) {
        if (!this.followRepository.existsByStreamerIdAndFollowerId(streamerId, followerId)) {
            Follow saved = this.followRepository.save(
                    Follow.builder()
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
        if (this.followRepository.existsByStreamerIdAndFollowerId(streamerId, followerId)) {
            this.followRepository.deleteFollowUpByStreamerIdAndFollowerId(streamerId, followerId);
            log.info("FollowUp successfully deleted");
            return SUCCESSFULLY_UNFOLLOW.getMessage();

        } else {
            log.error("You need to follow the streamer before unfollow it");
            throw new StillNotFollowException(STILL_NOT_FOLLOW.getMessage());
        }
    }
}
