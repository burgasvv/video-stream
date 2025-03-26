package org.burgas.backendserver.controller;

import org.burgas.backendserver.entity.Image;
import org.burgas.backendserver.service.ImageService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.parseMediaType;

@Controller
@RequestMapping(value = "/images")
@CrossOrigin(value = "http://localhost:4200")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(value = "/by-id")
    public @ResponseBody ResponseEntity<Resource> getImageById(@RequestParam Long imageId) {
        Image image = this.imageService.findById(imageId);
        return ResponseEntity
                .status(OK)
                .contentType(parseMediaType(image.getContentType()))
                .body(new InputStreamResource(
                        new ByteArrayInputStream(image.getData()))
                );
    }
}
