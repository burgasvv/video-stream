package org.burgas.backendserver.service;

import org.burgas.backendserver.entity.Image;
import org.burgas.backendserver.exception.FileEmptyException;
import org.burgas.backendserver.repository.ImageRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static java.util.Objects.requireNonNull;
import static java.util.concurrent.CompletableFuture.*;
import static org.burgas.backendserver.message.ImageMessage.FILE_EMPTY_OR_NOT_FOUND;
import static org.springframework.transaction.annotation.Isolation.*;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

@Service
@Transactional(readOnly = true, propagation = REQUIRED)
public class ImageService {

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image findById(Long imageId) {
        return this.imageRepository
                .findById(imageId)
                .orElseGet(Image::new);
    }

    @Async
    public CompletableFuture<Image> findByIdAsync(final Long imageId) {
        return supplyAsync(() -> this.imageRepository.findById(imageId))
                .thenApplyAsync(image -> image.orElseGet(Image::new));
    }

    @Transactional(
            isolation = READ_COMMITTED, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Image uploadImage(final MultipartFile multipartFile) throws IOException {
        if (multipartFile != null && !multipartFile.isEmpty()) {
            return this.imageRepository
                    .save(
                            Image.builder()
                                    .name(multipartFile.getOriginalFilename())
                                    .contentType(multipartFile.getContentType())
                                    .size(multipartFile.getSize())
                                    .format(requireNonNull(multipartFile.getContentType()).split("/")[1])
                                    .data(multipartFile.getBytes())
                                    .build()
                    );
        } else {
            throw new FileEmptyException(FILE_EMPTY_OR_NOT_FOUND.getMessage());
        }
    }

    @Transactional(
            isolation = REPEATABLE_READ, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public void deleteImage(Long imageId) {
        this.imageRepository.deleteById(imageId);
    }
}
