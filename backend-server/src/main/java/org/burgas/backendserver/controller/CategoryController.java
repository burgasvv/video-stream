package org.burgas.backendserver.controller;

import org.burgas.backendserver.entity.Category;
import org.burgas.backendserver.exception.WrongFileFormatException;
import org.burgas.backendserver.service.CategoryService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.net.URI.create;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static org.burgas.backendserver.message.IdentityMessage.WRONG_FILE_FORMAT;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping("/categories")
@CrossOrigin(value = "http://localhost:4200")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public @ResponseBody ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.categoryService.findAll());
    }

    @GetMapping(value = "/async")
    public @ResponseBody ResponseEntity<List<Category>> getAllCategoriesAsync()
            throws ExecutionException, InterruptedException {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.categoryService.findAllAsync().get());
    }

    @GetMapping(value = "/sse")
    public @ResponseBody ResponseEntity<SseEmitter> getAllCategoriesOnSse() {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_EVENT_STREAM, UTF_8))
                .body(this.categoryService.findAllInSse());
    }

    @GetMapping(value = "/stream")
    public @ResponseBody ResponseEntity<StreamingResponseBody> getAllCategoriesStream() {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_EVENT_STREAM, UTF_8))
                .body(this.categoryService.findAllInStream());
    }

    @GetMapping(value = "/by-id")
    public @ResponseBody ResponseEntity<Category> getCategoryById(@RequestParam Long categoryId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.categoryService.findById(categoryId));
    }

    @GetMapping(value = "/async/by-id")
    public @ResponseBody ResponseEntity<Category> getCategoryByIdAsync(@RequestParam final Long categoryId)
            throws ExecutionException, InterruptedException {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.categoryService.findByIdAsync(categoryId).get());
    }

    @PostMapping(value = "/create")
    public @ResponseBody ResponseEntity<Long> createCategory(@RequestBody Category category) {
        Long categoryId = this.categoryService.createOrUpdate(category);
        return ResponseEntity
                .status(FOUND)
                .location(create("/categories/by-id?categoryId=" + categoryId))
                .body(categoryId);
    }

    @PostMapping(value = "/async/create")
    public @ResponseBody ResponseEntity<Long> createCategoryAsync(@RequestBody final Category category)
            throws ExecutionException, InterruptedException {
        Long categoryId = this.categoryService.createOrUpdateAsync(category).get();
        return ResponseEntity
                .status(FOUND)
                .location(create("/categories/async/by-id?categoryId=" + categoryId))
                .body(categoryId);
    }

    @PostMapping(value = "/update")
    public @ResponseBody ResponseEntity<Long> updateCategory(@RequestBody Category category) {
        Long categoryId = this.categoryService.createOrUpdate(category);
        return ResponseEntity
                .status(FOUND)
                .location(create("/categories/by-id?categoryId=" + categoryId))
                .body(categoryId);
    }

    @PostMapping(value = "/upload-set-image", consumes = MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody ResponseEntity<Category> uploadAndSetImage(
            @RequestPart String categoryId, @RequestPart MultipartFile file
    ) throws IOException {
        if (requireNonNull(file.getContentType()).startsWith("image")) {
            Category category = this.categoryService.uploadAndSetImage(Long.parseLong(categoryId), file);
            return ResponseEntity
                    .status(FOUND)
                    .location(create("http://localhost:8888/categories/by-id?categoryId=" + category.getId()))
                    .body(category);
        } else {
            throw new WrongFileFormatException(WRONG_FILE_FORMAT.getMessage());
        }
    }

    @PostMapping(value = "/change-set-image", consumes = MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody ResponseEntity<Category> changeAndSetImage(
            @RequestPart String categoryId, @RequestPart MultipartFile file
    )  {
        if (requireNonNull(file.getContentType()).startsWith("image")) {
            Category category = this.categoryService.changeImage(Long.valueOf(categoryId), file);
            return ResponseEntity
                    .status(FOUND)
                    .location(create("http://localhost:8888/categories/by-id?categoryId=" + category.getId()))
                    .body(category);
        } else {
            throw new WrongFileFormatException(WRONG_FILE_FORMAT.getMessage());
        }
    }

    @DeleteMapping(value = "/delete-image")
    public @ResponseBody ResponseEntity<String> deleteImage(@RequestParam Long categoryId) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.categoryService.deleteImage(categoryId));
    }
}