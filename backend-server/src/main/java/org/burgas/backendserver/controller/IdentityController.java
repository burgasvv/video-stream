package org.burgas.backendserver.controller;

import org.burgas.backendserver.dto.IdentityRequest;
import org.burgas.backendserver.dto.IdentityResponse;
import org.burgas.backendserver.exception.WrongFileFormatException;
import org.burgas.backendserver.service.IdentityService;
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
@RequestMapping(value = "/identities")
@CrossOrigin(value = "http:/localhost:4200")
public class IdentityController {

    private final IdentityService identityService;

    public IdentityController(IdentityService identityService) {
        this.identityService = identityService;
    }

    @GetMapping
    public @ResponseBody ResponseEntity<List<IdentityResponse>> getAllIdentities() {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(identityService.findAll());
    }

    @GetMapping(value = "/sse")
    public @ResponseBody ResponseEntity<SseEmitter> getAllIdentitiesSse() {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_EVENT_STREAM, UTF_8))
                .body(this.identityService.findAllSse());
    }

    @GetMapping(value = "/stream")
    public @ResponseBody ResponseEntity<StreamingResponseBody> getAllIdentitiesAsStream() {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_EVENT_STREAM, UTF_8))
                .body(this.identityService.findAllByStream());
    }

    @GetMapping(value = "/async")
    public @ResponseBody ResponseEntity<List<IdentityResponse>> getAllIdentitiesAsync()
            throws ExecutionException, InterruptedException {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.identityService.findAllAsync().get());
    }

    @GetMapping(value = "/by-id", produces = APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<IdentityResponse> getIdentityById(@RequestParam Long identityId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(identityService.findById(identityId));
    }

    @GetMapping(value = "/async/by-id")
    public @ResponseBody ResponseEntity<IdentityResponse> getIdentityByIdAsync(@RequestParam Long identityId)
            throws ExecutionException, InterruptedException {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.identityService.findByIdAsync(identityId).get());
    }

    @PostMapping(value = "/create")
    public @ResponseBody ResponseEntity<IdentityResponse> createIdentity(@RequestBody IdentityRequest identityRequest) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(identityService.createOrUpdate(identityRequest));
    }

    @PostMapping(value = "/async/create")
    public @ResponseBody ResponseEntity<IdentityResponse> createIdentityAsync(
            @RequestBody final IdentityRequest identityRequest
    ) throws ExecutionException, InterruptedException {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.identityService.createOrUpdateAsync(identityRequest).get());
    }

    @PutMapping(value = "/update")
    public @ResponseBody ResponseEntity<IdentityResponse> updateIdentity(
            @RequestBody IdentityRequest identityRequest, @RequestParam Long identityId
    ) {
        identityRequest.setId(identityId);
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(identityService.createOrUpdate(identityRequest));
    }

    @PutMapping(value = "/async/update")
    public @ResponseBody ResponseEntity<IdentityResponse> updateIdentityAsync(
            @RequestBody final IdentityRequest identityRequest, @RequestParam final Long identityId
    ) throws ExecutionException, InterruptedException {
        identityRequest.setId(identityId);
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.identityService.createOrUpdateAsync(identityRequest).get());
    }

    @PostMapping(value = "/upload-set-image", consumes = MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody ResponseEntity<IdentityResponse> uploadAndSetImage(
            @RequestPart String identityId, @RequestPart MultipartFile file
    ) throws IOException {
        if (requireNonNull(file.getContentType()).startsWith("image")) {
            IdentityResponse identityResponse = this.identityService.uploadAndSetImage(Long.parseLong(identityId), file);
            return ResponseEntity
                    .status(FOUND)
                    .location(create("http://localhost:8888/identities/by-id?identityId=" + identityResponse.getId()))
                    .body(identityResponse);
        } else {
            throw new WrongFileFormatException(WRONG_FILE_FORMAT.getMessage());
        }
    }

    @PostMapping(value = "/change-set-image", consumes = MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody ResponseEntity<IdentityResponse> changeAndSetImage(
            @RequestPart String identityId, @RequestPart MultipartFile file
    ) {
        if (requireNonNull(file.getContentType()).startsWith("image")) {
            IdentityResponse identityResponse = this.identityService.changeImage(Long.parseLong(identityId), file);
            return ResponseEntity
                    .status(FOUND)
                    .location(create("http://localhost:8888/identities/by-id?identityId=" + identityResponse.getId()))
                    .body(identityResponse);
        } else {
            throw new WrongFileFormatException(WRONG_FILE_FORMAT.getMessage());
        }
    }

    @DeleteMapping(value = "/delete-image")
    public @ResponseBody ResponseEntity<String> deleteIdentityImage(@RequestParam Long identityId) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.identityService.deleteImage(identityId));
    }
}
