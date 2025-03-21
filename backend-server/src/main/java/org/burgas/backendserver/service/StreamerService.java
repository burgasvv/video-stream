package org.burgas.backendserver.service;

import org.burgas.backendserver.dto.StreamerRequest;
import org.burgas.backendserver.dto.StreamerResponse;
import org.burgas.backendserver.entity.Image;
import org.burgas.backendserver.entity.Streamer;
import org.burgas.backendserver.entity.StreamerCategory;
import org.burgas.backendserver.exception.StreamerCategoryDataEmptyException;
import org.burgas.backendserver.mapper.StreamerMapper;
import org.burgas.backendserver.repository.StreamerCategoryRepository;
import org.burgas.backendserver.repository.StreamerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.burgas.backendserver.message.StreamerMessage.*;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

@Service
@Transactional(readOnly = true, propagation = REQUIRED)
public class StreamerService {

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

    public StreamerResponse findById(Long streamId) {
        return this.streamerRepository
                .findById(streamId)
                .map(streamerMapper::toStreamerResponse)
                .orElseGet(StreamerResponse::new);
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
