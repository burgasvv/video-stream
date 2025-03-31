package org.burgas.backendserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.burgas.backendserver.dto.StreamerRequest;
import org.burgas.backendserver.dto.StreamerResponse;
import org.burgas.backendserver.entity.Image;
import org.burgas.backendserver.entity.Streamer;
import org.burgas.backendserver.entity.StreamerCategory;
import org.burgas.backendserver.exception.StreamerCategoryDataEmptyException;
import org.burgas.backendserver.mapper.StreamerMapper;
import org.burgas.backendserver.repository.StreamerCategoryRepository;
import org.burgas.backendserver.repository.StreamerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static java.lang.Thread.ofVirtual;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.burgas.backendserver.message.StreamerMessage.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

@Service
@Transactional(readOnly = true, propagation = REQUIRED)
public class StreamerService {

    private static final Logger log = LoggerFactory.getLogger(StreamerService.class);
    private final StreamerRepository streamerRepository;
    private final StreamerMapper streamerMapper;
    private final ImageService imageService;
    private final StreamerCategoryRepository streamerCategoryRepository;

    public StreamerService(
            StreamerRepository streamerRepository,
            StreamerMapper streamerMapper, ImageService imageService,
            StreamerCategoryRepository streamerCategoryRepository
    ) {
        this.streamerRepository = streamerRepository;
        this.streamerMapper = streamerMapper;
        this.imageService = imageService;
        this.streamerCategoryRepository = streamerCategoryRepository;
    }

    public List<StreamerResponse> findAll() {
        return this.streamerRepository
                .findAll()
                .stream()
                .map(streamerMapper::toStreamerResponse)
                .toList();
    }

    public SseEmitter findAllSse() {
        SseEmitter sseEmitter = new SseEmitter();
        runAsync(
                () -> {
                    this.streamerRepository
                            .findAll()
                            .forEach(
                                    streamer ->
                                    {
                                        try {
                                            SECONDS.sleep(1);
                                            sendEventAboutStreamer(streamer, sseEmitter);

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

    private static void sendEventAboutStreamer(Streamer streamer, SseEmitter sseEmitter) throws IOException {
        sseEmitter.send(
                SseEmitter.event()
                        .id(String.valueOf(streamer.getId()))
                        .name(streamer.getFirstname() + " " + streamer.getLastname())
                        .comment("New Comment")
                        .data(streamer, APPLICATION_JSON)
                        .build()
        );
    }

    public StreamingResponseBody findAllInStream() {
        return outputStream ->
            this.streamerRepository
                    .findAll()
                    .forEach(
                            streamer -> {
                                try {
                                    writeStream(outputStream, streamer);

                                } catch (IOException | InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    );
    }

    private static void writeStream(OutputStream outputStream, Streamer streamer)
            throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        String streamerString = objectMapper.writeValueAsString(streamer) + "\n";
        outputStream.write(streamerString.getBytes(UTF_8));
        outputStream.flush();
        SECONDS.sleep(1);
    }

    @Async
    public CompletableFuture<List<StreamerResponse>> findAllAsync() {
        return supplyAsync(this.streamerRepository::findAll)
                .thenApplyAsync(
                        streamers -> streamers
                                .stream()
                                .map(streamerMapper::toStreamerResponse)
                                .toList()
                );
    }

    public List<StreamerResponse> findStreamersByFollowerId(final Long followerId) {
        return this.streamerRepository
                .findStreamersByFollowerId(followerId)
                .stream()
                .peek(streamer -> log.info("Find streamer by follower Id: {}", streamer))
                .map(streamerMapper::toStreamerResponse)
                .toList();
    }

    public SseEmitter findStreamersByFollowerIdSse(final Long followerId) {
        SseEmitter sseEmitter = new SseEmitter();
        ofVirtual()
                .start(
                        () -> {
                            this.streamerRepository
                                    .findStreamersByFollowerId(followerId)
                                    .stream()
                                    .peek(streamer -> log.info("Find streamer by follower Id in sse: {}", streamer))
                                    .map(streamerMapper::toStreamerResponse)
                                    .forEach(
                                            streamerResponse -> {
                                                try {
                                                    Set<ResponseBodyEmitter.DataWithMediaType> data = SseEmitter.event()
                                                            .data(streamerResponse, APPLICATION_JSON)
                                                            .build();
                                                    sseEmitter.send(data);
                                                    log.info("Follower data was sent by server: {}", data);
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

    public StreamingResponseBody findStreamersByFollowerIdStream(final Long followerId) {
        return outputStream ->
                this.streamerRepository
                        .findStreamersByFollowerId(followerId)
                        .stream()
                        .peek(streamer -> log.info("Find streamer by follower Id in stream: {}", streamer))
                        .map(streamerMapper::toStreamerResponse)
                        .forEach(
                                streamerResponse -> {
                                    try {
                                        outputStream.write((streamerResponse.toString() + "\n").getBytes(UTF_8));
                                        outputStream.flush();
                                        log.info("Write streamer object to stream");
                                        SECONDS.sleep(1);

                                    } catch (IOException | InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                        );
    }

    public StreamerResponse findById(Long streamId) {
        return this.streamerRepository
                .findById(streamId)
                .map(streamerMapper::toStreamerResponse)
                .orElseGet(StreamerResponse::new);
    }

    @Async
    public CompletableFuture<StreamerResponse> findByIdAsync(final Long streamerId) {
        return supplyAsync(() -> this.streamerRepository.findById(streamerId))
                .thenApplyAsync(
                        streamer -> streamer.map(streamerMapper::toStreamerResponse)
                )
                .thenApplyAsync(
                        streamerResponse -> streamerResponse.orElseGet(StreamerResponse::new)
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Long createOrUpdate(final StreamerRequest streamerRequest) {
        return this.streamerMapper
                .toStreamerSave(streamerRequest)
                .getId();
    }

    @Async
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<Long> createOrUpdateAsync(final StreamerRequest streamerRequest) {
        return supplyAsync(() -> this.streamerMapper.toStreamerSave(streamerRequest).getId());
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Long addCategories(final StreamerRequest streamerRequest) {
        if (streamerRequest.getId() != null && streamerRequest.getCategoryIds() != null) {
            streamerRequest
                    .getCategoryIds()
                    .forEach(
                            categoryId -> {
                                if (!this.streamerCategoryRepository
                                                .existsStreamerCategoryByStreamerIdAndCategoryId(
                                                        streamerRequest.getId(), categoryId)
                                ) {
                                    this.streamerCategoryRepository.save(
                                            StreamerCategory.builder()
                                                    .streamerId(streamerRequest.getId())
                                                    .categoryId(categoryId)
                                                    .addedAt(LocalDateTime.now())
                                                    .build()
                                    );

                                } else {
                                    StreamerCategory streamerCategory = this.streamerCategoryRepository
                                            .findStreamerCategoryByStreamerIdAndCategoryId(streamerRequest.getId(), categoryId);
                                    streamerCategory.setAddedAt(LocalDateTime.now());
                                    this.streamerCategoryRepository
                                            .save(streamerCategory);
                                }
                            }
                    );

            return this.findById(streamerRequest.getId()).getId();

        } else {
            throw new StreamerCategoryDataEmptyException(STREAMER_CATEGORY_DATA_EMPTY.getMessage());
        }
    }

    @Async
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<Long> addCategoriesAsync(final StreamerRequest streamerRequest) {
        return supplyAsync(() -> this.addCategories(streamerRequest));
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public StreamerResponse uploadAndSetImage(Long streamerId, final MultipartFile multipartFile) throws IOException {
        Image image = this.imageService.uploadImage(multipartFile);
        Streamer streamer = this.streamerRepository.findById(streamerId).orElse(null);

        if (streamer != null) {
            streamer.setImageId(image.getId());
            return this.streamerMapper
                    .toStreamerResponse(this.streamerRepository
                            .save(streamer));

        } else {
            throw new RuntimeException(STREAMER_NOT_FOUND.getMessage());
        }
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public StreamerResponse changeAndSetImage(final Long streamerId, final MultipartFile multipartFile) {
        return this.streamerRepository
                .findById(streamerId)
                .filter(streamer -> streamer.getImageId() != null)
                .map(
                        streamer -> {
                            try {
                                this.imageService.deleteImage(streamer.getImageId());
                                return this.uploadAndSetImage(streamer.getId(), multipartFile);

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .orElseThrow(() -> new RuntimeException(STREAMER_IMAGE_NOT_FOUND.getMessage()));
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteImage(final Long streamerId) {
        return this.streamerRepository
                .findById(streamerId)
                .map(
                        streamer -> {
                            this.imageService.deleteImage(streamer.getImageId());
                            return STREAMER_IMAGE_DELETED.getMessage();
                        }
                )
                .orElseThrow(() -> new RuntimeException(STREAMER_NOT_FOUND.getMessage()));
    }
}
