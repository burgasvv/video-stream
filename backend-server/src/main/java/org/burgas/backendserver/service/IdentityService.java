package org.burgas.backendserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.burgas.backendserver.dto.IdentityRequest;
import org.burgas.backendserver.dto.IdentityResponse;
import org.burgas.backendserver.entity.Identity;
import org.burgas.backendserver.entity.Image;
import org.burgas.backendserver.mapper.IdentityMapper;
import org.burgas.backendserver.repository.IdentityRepository;
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
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static java.lang.Thread.ofVirtual;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.burgas.backendserver.message.IdentityMessage.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

@Service
@Transactional(readOnly = true, propagation = REQUIRED)
public class IdentityService {

    private static final Logger log = LoggerFactory.getLogger(IdentityService.class);
    private final IdentityRepository identityRepository;
    private final IdentityMapper identityMapper;
    private final ImageService imageService;

    public IdentityService(
            IdentityRepository identityRepository,
            IdentityMapper identityMapper, ImageService imageService
    ) {
        this.identityRepository = identityRepository;
        this.identityMapper = identityMapper;
        this.imageService = imageService;
    }

    public List<IdentityResponse> findAll() {
        return this.identityRepository
                .findAll()
                .stream()
                .peek(identity -> log.info("Find identity of all: {}", identity))
                .map(identityMapper::toIdentityResponse)
                .toList();
    }

    public SseEmitter findAllSse() {
        SseEmitter sseEmitter = new SseEmitter();
        ofVirtual()
                .start(
                        () ->{
                            this.identityRepository
                                    .findAll()
                                    .forEach(
                                            identity ->
                                            {
                                                try {
                                                    SECONDS.sleep(1);
                                                    log.info("Find identity in sse: {}", identity);
                                                    Set<ResponseBodyEmitter.DataWithMediaType> data = SseEmitter.event()
                                                            .id(String.valueOf(identity.getId()))
                                                            .name(identity.getNickname())
                                                            .comment("New comment for: " + identity.getNickname())
                                                            .data(identity, APPLICATION_JSON)
                                                            .build();
                                                    sseEmitter.send(data);
                                                    log.info("Event was send: {}", data);

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

    public StreamingResponseBody findAllByStream() {
        return outputStream ->
            this.identityRepository
                    .findAll()
                    .forEach(
                            identity -> {
                                try {
                                    log.info("Identity was found: {}", identity);
                                    writeIdentityInStream(outputStream, identity);

                                } catch (IOException | InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    );
    }



    private static void writeIdentityInStream(OutputStream outputStream, Identity identity)
            throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        String identityString = objectMapper.writeValueAsString(identity) + "\n";
        outputStream.write(identityString.getBytes(UTF_8));
        outputStream.flush();
        SECONDS.sleep(1);
        log.info("Identity was written in stream: {}", identityString);
    }

    @Async
    public CompletableFuture<List<IdentityResponse>> findAllAsync() {
        return supplyAsync(this.identityRepository::findAll)
                .thenApplyAsync(
                        identities -> identities
                                .stream()
                                .peek(identity -> log.info("Identity of all was found async: {}", identity))
                                .map(this.identityMapper::toIdentityResponse)
                                .toList()
                );
    }

    public List<IdentityResponse> findFollowersByStreamerId(final Long streamerId) {
        return this.identityRepository
                .findFollowersByStreamerId(streamerId)
                .stream()
                .peek(identity -> log.info("Find follower by streamer Id: {}", identity))
                .map(identityMapper::toIdentityResponse)
                .toList();
    }

    public SseEmitter findFollowersByStreamerIdSse(final Long streamerId) {
        SseEmitter sseEmitter = new SseEmitter();
        ofVirtual()
                .start(
                        () -> {
                            this.identityRepository
                                    .findFollowersByStreamerId(streamerId)
                                    .stream()
                                    .peek(identity -> log.info("Find follower by streamer: {}", identity))
                                    .map(identityMapper::toIdentityResponse)
                                    .forEach(
                                            identityResponse -> {
                                                try {
                                                    Set<ResponseBodyEmitter.DataWithMediaType> data = SseEmitter.event()
                                                            .data(identityResponse, APPLICATION_JSON)
                                                            .build();
                                                    sseEmitter.send(data);

                                                    log.info("Follower data send as event: {}", data);
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

    public StreamingResponseBody findFollowersByStreamerIdStream(final Long streamerId) {
        return outputStream ->
                this.identityRepository
                        .findFollowersByStreamerId(streamerId)
                        .stream()
                        .peek(identity -> log.info("Find follower by streamer id in stream: {}", identity))
                        .map(identityMapper::toIdentityResponse)
                        .forEach(
                                identityResponse -> {
                                    try {
                                        outputStream.write((identityResponse.toString() + "\n").getBytes(UTF_8));
                                        outputStream.flush();
                                        log.info("Write follower object in stream: {}", identityResponse);
                                        SECONDS.sleep(1);

                                    } catch (IOException | InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                        );
    }

    public IdentityResponse findById(Long identityId) {
        return this.identityRepository
                .findById(identityId)
                .stream()
                .peek(identity -> log.info("Identity by id was found: {}", identity))
                .map(identityMapper::toIdentityResponse)
                .findFirst()
                .orElseGet(
                        () -> {
                            log.info("Identity by id was not found: Creating empty Object");
                            return new IdentityResponse();
                        }
                );

    }

    @Async
    public CompletableFuture<IdentityResponse> findByIdAsync(Long identityId) {
        return supplyAsync(() -> this.identityRepository.findById(identityId))
                .thenApplyAsync(
                        identity -> identity
                                .stream()
                                .peek(thisIdentity -> log.info("Identity was found async: {}", thisIdentity))
                                .map(identityMapper::toIdentityResponse)
                )
                .thenApplyAsync(
                        identityResponse -> identityResponse
                                .findFirst()
                                .orElseGet(
                                        () -> {
                                            log.info("Identity was not found: Create empty object");
                                            return new IdentityResponse();
                                        }
                                )
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public IdentityResponse createOrUpdate(IdentityRequest identityRequest) {
        Identity identity = identityRepository.save(identityMapper.toIdentity(identityRequest));
        log.info("Identity created: {}", identity);
        return this.identityMapper.toIdentityResponse(identity);
    }

    @Async
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<IdentityResponse> createOrUpdateAsync(final IdentityRequest identityRequest) {
        return supplyAsync(
                        () -> {
                            Identity identity = this.identityRepository.save(
                                    this.identityMapper.toIdentity(identityRequest));
                            log.info("Identity created async: {}", identity);
                            return identity;
                        }
                )
                .thenApplyAsync(this.identityMapper::toIdentityResponse);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public IdentityResponse uploadAndSetImage(Long identityId, final MultipartFile multipartFile) throws IOException {
        Image image = this.imageService.uploadImage(multipartFile);
        Identity identity = this.identityRepository.findById(identityId).orElse(null);

        log.info("Image uploaded: {}", image);

        if (identity != null) {
            identity.setImageId(image.getId());
            return this.identityMapper
                    .toIdentityResponse(this.identityRepository.save(identity));
        } else {
            log.error("Identity not found for setting image");
            throw new RuntimeException(IDENTITY_NOT_FOUND.getMessage());
        }
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public IdentityResponse changeImage(Long identityId, final MultipartFile multipartFile) {
        return this.identityRepository
                .findById(identityId)
                .filter(identity -> identity.getImageId() != null)
                .map(
                        identity -> {
                            try {
                                this.imageService.deleteImage(identity.getImageId());
                                return this.uploadAndSetImage(identity.getId(), multipartFile);

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .orElseThrow(
                        () -> {
                            log.error("Identity not found. Could not change image");
                            return new RuntimeException(IDENTITY_IMAGE_NOT_FOUND.getMessage());
                        }
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteImage(Long identityId) {
        return this.identityRepository
                .findById(identityId)
                .filter(identity -> identity.getImageId() != null)
                .map(
                        identity -> {
                            this.imageService.deleteImage(identity.getImageId());
                            log.info("Image was deleted");
                            return IDENTITY_IMAGE_DELETED.getMessage();
                        }
                )
                .orElseThrow(
                        () -> {
                            log.error("Identity not found. Could not delete image");
                            return new RuntimeException(IDENTITY_IMAGE_NOT_FOUND.getMessage());
                        }
                );
    }
}
