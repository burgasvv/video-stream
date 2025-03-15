package org.burgas.videoservice.controller;

import org.burgas.videoservice.entity.Category;
import org.burgas.videoservice.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.net.URI.create;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping("/categories")
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

    @GetMapping(value = "/by-id")
    public @ResponseBody ResponseEntity<Category> getCategoryById(@RequestParam Long categoryId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.categoryService.findById(categoryId));
    }

    @PostMapping(value = "/create")
    public @ResponseBody ResponseEntity<Long> createCategory(@RequestBody Category category) {
        Long categoryId = this.categoryService.createOrUpdate(category);
        return ResponseEntity
                .status(FOUND)
                .location(create("/categories/by-id?categoryId=" + categoryId))
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
}