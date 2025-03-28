package org.burgas.backendserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.burgas.backendserver.dto.IdentityRequest;
import org.burgas.backendserver.dto.IdentityResponse;
import org.burgas.backendserver.entity.Identity;
import org.burgas.backendserver.entity.Image;
import org.burgas.backendserver.mapper.IdentityMapper;
import org.burgas.backendserver.repository.IdentityRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
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
                                                    sseEmitter.send(
                                                            SseEmitter.event()
                                                                    .id(String.valueOf(identity.getId()))
                                                                    .name(identity.getNickname())
                                                                    .comment("New comment for: " + identity.getNickname())
                                                                    .data(identity, APPLICATION_JSON)
                                                                    .build()
                                                    );
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
        return outputStream -> {
            this.identityRepository
                    .findAll()
                    .forEach(
                            identity -> {
                                try {
                                    writeIdentityInStream(outputStream, identity);

                                } catch (IOException | InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    );
        };
    }

    private static void writeIdentityInStream(OutputStream outputStream, Identity identity)
            throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        String identityString = objectMapper.writeValueAsString(identity) + "\n";
        outputStream.write(identityString.getBytes(UTF_8));
        outputStream.flush();
        SECONDS.sleep(1);
    }

    @Async
    public CompletableFuture<List<IdentityResponse>> findAllAsync() {
        return supplyAsync(this.identityRepository::findAll)
                .thenApplyAsync(
                        identities -> identities
                                .stream()
                                .map(this.identityMapper::toIdentityResponse)
                                .toList()
                );
    }

    public IdentityResponse findById(Long identityId) {
        return this.identityRepository
                .findById(identityId)
                .map(identityMapper::toIdentityResponse)
                .orElseGet(IdentityResponse::new);

    }

    @Async
    public CompletableFuture<IdentityResponse> findByIdAsync(Long identityId) {
        return supplyAsync(() -> this.identityRepository.findById(identityId))
                .thenApplyAsync(
                        identity -> identity
                                .map(identityMapper::toIdentityResponse)
                )
                .thenApplyAsync(
                        identityResponse -> identityResponse
                                .orElseGet(IdentityResponse::new)
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public IdentityResponse createOrUpdate(IdentityRequest identityRequest) {
        return identityMapper
                .toIdentityResponse(
                        identityRepository.save(identityMapper.toIdentity(identityRequest))
                );
    }

    @Async
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<IdentityResponse> createOrUpdateAsync(final IdentityRequest identityRequest) {
        return supplyAsync(
                        () -> this.identityRepository.save(
                                this.identityMapper.toIdentity(identityRequest)
                        )
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

        if (identity != null) {
            identity.setImageId(image.getId());
            return this.identityMapper
                    .toIdentityResponse(this.identityRepository.save(identity));
        } else {
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
                .orElseThrow(() -> new RuntimeException(IDENTITY_IMAGE_NOT_FOUND.getMessage()));
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
                            return IDENTITY_IMAGE_DELETED.getMessage();
                        }
                )
                .orElseThrow(() -> new RuntimeException(IDENTITY_IMAGE_NOT_FOUND.getMessage()));
    }
}
