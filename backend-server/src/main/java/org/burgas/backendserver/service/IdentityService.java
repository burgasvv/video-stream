package org.burgas.backendserver.service;

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

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.burgas.backendserver.message.IdentityMessage.*;
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
