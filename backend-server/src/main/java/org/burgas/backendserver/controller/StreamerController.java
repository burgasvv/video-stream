package org.burgas.backendserver.controller;

import org.burgas.backendserver.dto.StreamerRequest;
import org.burgas.backendserver.dto.StreamerResponse;
import org.burgas.backendserver.exception.WrongFileFormatException;
import org.burgas.backendserver.service.StreamerService;
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
import static org.burgas.backendserver.message.StreamerMessage.WRONG_FILE_FORMAT;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping("/streamers")
@CrossOrigin(value = "http://localhost:4200")
public class StreamerController {

    private final StreamerService streamerService;

    public StreamerController(StreamerService streamerService) {
        this.streamerService = streamerService;
    }

    @GetMapping
    public @ResponseBody ResponseEntity<List<StreamerResponse>> getAllStreamers() {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(streamerService.findAll());
    }

    @GetMapping(value = "/sse")
    public @ResponseBody ResponseEntity<SseEmitter> getStreamersBySse() {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_EVENT_STREAM, UTF_8))
                .body(this.streamerService.findAllSse());
    }

    @GetMapping(value = "/stream")
    public @ResponseBody ResponseEntity<StreamingResponseBody> getStreamersInStream() {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_EVENT_STREAM, UTF_8))
                .body(this.streamerService.findAllInStream());
    }

    @GetMapping(value = "/async")
    public @ResponseBody ResponseEntity<List<StreamerResponse>> getAllStreamersAsync()
            throws ExecutionException, InterruptedException {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.streamerService.findAllAsync().get());
    }

    @GetMapping(value = "/by-id")
    public @ResponseBody ResponseEntity<StreamerResponse> getStreamerById(@RequestParam Long streamerId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(streamerService.findById(streamerId));
    }

    @GetMapping(value = "/async/by-id")
    public @ResponseBody ResponseEntity<StreamerResponse> getStreamerByIdAsync(@RequestParam final Long streamerId)
            throws ExecutionException, InterruptedException {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.streamerService.findByIdAsync(streamerId).get());
    }

    @PostMapping(value = "/create")
    public @ResponseBody ResponseEntity<Long> createStreamer(
            @RequestBody StreamerRequest streamerRequest, @RequestParam Long identityId
    ) {
        streamerRequest.setIdentityId(identityId);
        Long streamerId = streamerService.createOrUpdate(streamerRequest);
        return ResponseEntity
                .status(FOUND)
                .location(create("/streamers/by-id?streamerId=" + streamerId))
                .body(streamerId);
    }

    @PostMapping(value = "/async/create")
    public @ResponseBody ResponseEntity<Long> createStreamerAsync(
            @RequestBody StreamerRequest streamerRequest, @RequestParam Long identityId
    ) throws ExecutionException, InterruptedException {
        streamerRequest.setIdentityId(identityId);
        Long streamerId = streamerService.createOrUpdateAsync(streamerRequest).get();
        return ResponseEntity
                .status(FOUND)
                .location(create("/streamers/by-id?streamerId=" + streamerId))
                .body(streamerId);
    }

    @PostMapping(value = "/update")
    public @ResponseBody ResponseEntity<Long> updateStreamer(
            @RequestBody StreamerRequest streamerRequest, @RequestParam Long streamerId
    ) {
        streamerRequest.setId(streamerId);
        Long id = streamerService.createOrUpdate(streamerRequest);
        return ResponseEntity
                .status(FOUND)
                .location(create("/streamers/by-id?streamerId=" + id))
                .body(id);
    }

    @PostMapping(value = "/async/update")
    public @ResponseBody ResponseEntity<Long> updateStreamerAsync(
            @RequestBody StreamerRequest streamerRequest, @RequestParam Long streamerId
    ) throws ExecutionException, InterruptedException {
        streamerRequest.setId(streamerId);
        Long id = streamerService.createOrUpdateAsync(streamerRequest).get();
        return ResponseEntity
                .status(FOUND)
                .location(create("/streamers/by-id?streamerId=" + id))
                .body(id);
    }

    @PostMapping(value = "/add-categories")
    public @ResponseBody ResponseEntity<Long> addCategories(
            @RequestBody StreamerRequest streamerRequest, @RequestParam String streamerId
    ) {
        streamerRequest.setId(Long.parseLong(streamerId));
        Long redirectStreamerId = this.streamerService.addCategories(streamerRequest);
        return ResponseEntity
                .status(FOUND)
                .location(create("/streamers/by-id?streamerId=" + redirectStreamerId))
                .body(redirectStreamerId);
    }

    @PostMapping(value = "/async/add-categories")
    public @ResponseBody ResponseEntity<Long> addCategoriesAsync(
            @RequestBody StreamerRequest streamerRequest, @RequestParam String streamerId
    ) throws ExecutionException, InterruptedException {
        streamerRequest.setId(Long.parseLong(streamerId));
        Long redirectStreamerId = this.streamerService.addCategoriesAsync(streamerRequest).get();
        return ResponseEntity
                .status(FOUND)
                .location(create("/streamers/by-id?streamerId=" + redirectStreamerId))
                .body(redirectStreamerId);
    }

    @PostMapping(value = "/upload-set-image", consumes = MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody ResponseEntity<StreamerResponse> uploadAndSetImage(
            @RequestPart String streamerId, @RequestPart MultipartFile file
    ) throws IOException {
        if (requireNonNull(file.getContentType()).startsWith("image")) {
            StreamerResponse streamerResponse = this.streamerService.uploadAndSetImage(Long.valueOf(streamerId), file);
            return ResponseEntity
                    .status(FOUND)
                    .location(create("http://localhost:8888/streamers/by-id?streamerId=" + streamerResponse.getId()))
                    .body(streamerResponse);

        } else {
            throw new WrongFileFormatException(WRONG_FILE_FORMAT.getMessage());
        }
    }

    @PostMapping(value = "/change-set-image", consumes = MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody ResponseEntity<StreamerResponse> changeAndSetImage(
            @RequestPart String streamerId, @RequestPart MultipartFile file
    ) {
        if (requireNonNull(file.getContentType()).startsWith("image")) {
            StreamerResponse streamerResponse = this.streamerService.changeAndSetImage(Long.valueOf(streamerId), file);
            return ResponseEntity
                    .status(FOUND)
                    .location(create("http://localhost:8888/streamers/by-id?streamerId=" + streamerResponse.getId()))
                    .body(streamerResponse);

        } else {
            throw new WrongFileFormatException(WRONG_FILE_FORMAT.getMessage());
        }
    }

    @DeleteMapping(value = "/delete-image")
    public @ResponseBody ResponseEntity<String> deleteImage(@RequestParam Long streamerId) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.streamerService.deleteImage(streamerId));
    }
}
