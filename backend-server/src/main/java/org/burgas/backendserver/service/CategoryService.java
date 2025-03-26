package org.burgas.backendserver.service;

import org.burgas.backendserver.entity.Category;
import org.burgas.backendserver.entity.Image;
import org.burgas.backendserver.exception.CategoryForImageNotFoundException;
import org.burgas.backendserver.exception.CategoryOrImageUndefinedException;
import org.burgas.backendserver.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

import static java.lang.String.valueOf;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.burgas.backendserver.message.CategoryMessage.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.web.servlet.mvc.method.annotation.SseEmitter.*;

@Service
@Transactional(readOnly = true, propagation = REQUIRED)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ImageService imageService;

    public CategoryService(CategoryRepository categoryRepository, ImageService imageService) {
        this.categoryRepository = categoryRepository;
        this.imageService = imageService;
    }

    public List<Category> findAll() {
        return this.categoryRepository.findAll();
    }

    public SseEmitter findAllInSse() {
        SseEmitter sseEmitter = new SseEmitter();
        runAsync(
                () -> {
                        this.categoryRepository
                                .findAll()
                                .forEach(
                                        category -> {
                                            try {
                                                SECONDS.sleep(1);
                                                sseEmitter.send(
                                                        event()
                                                                .id(valueOf(category.getId()))
                                                                .name(category.getName())
                                                                .comment("new comment for: " + category.getName())
                                                                .data(category, APPLICATION_JSON)
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

    public Category findById(Long categoryId) {
        return this.categoryRepository
                .findById(categoryId)
                .orElseGet(Category::new);
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Long createOrUpdate(Category category) {
        return this.categoryRepository
                .save(category)
                .getId();
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Category uploadAndSetImage(Long categoryId, final MultipartFile multipartFile) throws IOException {
        Image image = this.imageService.uploadImage(multipartFile);
        Category category = this.categoryRepository.findById(categoryId).orElse(null);

        if (category != null) {
            category.setImageId(image.getId());
            return this.categoryRepository.save(category);

        } else {
            throw new CategoryForImageNotFoundException(CATEGORY_FOR_IMAGE_NOT_FOUND.getMessage());
        }
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Category changeImage(Long categoryId, final MultipartFile multipartFile) {
        return this.categoryRepository.findById(categoryId)
                .filter(category -> category.getImageId() != null)
                .map(
                        category -> {
                            try {
                                this.imageService.deleteImage(category.getImageId());
                                return this.uploadAndSetImage(category.getId(), multipartFile);

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .orElseThrow(() -> new CategoryOrImageUndefinedException(CATEGORY_OR_IMAGE_UNDEFINED.getMessage()));
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteImage(Long categoryId) {
        return this.categoryRepository.findById(categoryId)
                .filter(category -> category.getImageId() != null)
                .map(
                        category -> {
                            this.imageService.deleteImage(category.getImageId());
                            return CATEGORY_IMAGE_DELETED.getMessage();
                        }
                )
                .orElseThrow(() -> new CategoryOrImageUndefinedException(CATEGORY_OR_IMAGE_UNDEFINED.getMessage()));
    }
}
