package org.burgas.backendserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.burgas.backendserver.entity.Category;
import org.burgas.backendserver.entity.Image;
import org.burgas.backendserver.exception.CategoryForImageNotFoundException;
import org.burgas.backendserver.exception.CategoryOrImageUndefinedException;
import org.burgas.backendserver.listener.CategoryEvent;
import org.burgas.backendserver.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static java.lang.String.valueOf;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.burgas.backendserver.message.CategoryMessage.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event;

@Service
@Transactional(readOnly = true, propagation = REQUIRED)
public class CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository categoryRepository;
    private final ImageService imageService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public CategoryService(
            CategoryRepository categoryRepository, ImageService imageService,
            ApplicationEventPublisher applicationEventPublisher
    ) {
        this.categoryRepository = categoryRepository;
        this.imageService = imageService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public List<Category> findAll() {
        return this.categoryRepository
                .findAll()
                .stream()
                .peek(
                        category -> {
                            log.info("Find category of all: {}", category);
                            this.applicationEventPublisher
                                    .publishEvent(new CategoryEvent(category));
                        }
                )
                .toList();
    }

    @Async
    public CompletableFuture<List<Category>> findAllAsync() {
        return supplyAsync(this.categoryRepository::findAll)
                .thenApplyAsync(
                        categories -> categories
                                .stream()
                                .peek(
                                        category -> {
                                            log.info("Find category of all async: {}", category);
                                            this.applicationEventPublisher
                                                    .publishEvent(new CategoryEvent(category));
                                        }
                                )
                                .toList()
                );
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

                                                log.info("Find category of all sse: {}", category);
                                                Set<ResponseBodyEmitter.DataWithMediaType> data = event()
                                                        .id(valueOf(category.getId()))
                                                        .name(category.getName())
                                                        .comment("new comment for: " + category.getName())
                                                        .data(category, APPLICATION_JSON)
                                                        .build();
                                                sseEmitter.send(data);

                                                log.info("Sse event was send: {}", data);

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

    public StreamingResponseBody findAllInStream() {
        return outputStream ->
                this.categoryRepository
                        .findAll()
                        .forEach(
                                category -> {
                                    try {
                                        log.info("Find category of all in stream: {}", category);
                                        ObjectMapper objectMapper = new ObjectMapper();
                                        String categoryString = objectMapper.writeValueAsString(category) + "\n";
                                        outputStream.write(categoryString.getBytes(UTF_8));
                                        outputStream.flush();

                                        log.info("Object in stream was written: {}", categoryString);
                                        SECONDS.sleep(1);

                                    } catch (IOException | InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                        );
    }

    public Category findById(Long categoryId) {
        return this.categoryRepository
                .findById(categoryId)
                .map(
                        category -> {
                            log.info("Find category: {}", category);
                            return category;
                        }
                )
                .orElseGet(
                        () -> {
                            log.warn("Category nor found, create empty object");
                            return new Category();
                        }
                );
    }

    @Async
    public CompletableFuture<Category> findByIdAsync(final Long categoryId) {
        return supplyAsync(
                () -> this.categoryRepository.findById(categoryId)
                        .map(
                                category -> {
                                    log.info("Find category by id async: {}", category);
                                    return category;
                                }
                        )
        )
                .thenApplyAsync(
                        category -> category.orElseGet(
                                () -> {
                                    log.warn("Category not found, creating new empty object");
                                    return new Category();
                                }
                        )
                );
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Long createOrUpdate(Category category) {
        Category saved = this.categoryRepository.save(category);
        log.info("Category was created: {}", saved);
        return saved.getId();
    }

    @Async
    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CompletableFuture<Long> createOrUpdateAsync(final Category category) {
        return supplyAsync(() -> this.createOrUpdate(category));
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Category uploadAndSetImage(Long categoryId, final MultipartFile multipartFile) throws IOException {
        Image image = this.imageService.uploadImage(multipartFile);
        Category category = this.categoryRepository.findById(categoryId).orElse(null);

        log.info("Image was uploaded: {}", image);

        if (category != null) {
            log.info("Category was found: {}", category);
            category.setImageId(image.getId());
            return this.categoryRepository.save(category);

        } else {
            log.error("Category was not found");
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
                .orElseThrow(
                        () -> {
                            log.error("Category or image undefined");
                            return new CategoryOrImageUndefinedException(CATEGORY_OR_IMAGE_UNDEFINED.getMessage());
                        }
                );
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
                .orElseThrow(
                        () -> {
                            log.error("Category or image undefined exception");
                            return new CategoryOrImageUndefinedException(CATEGORY_OR_IMAGE_UNDEFINED.getMessage());
                        }
                );
    }
}
