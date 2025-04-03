package org.burgas.backendserver.controller;

import org.burgas.backendserver.dto.VideoRequest;
import org.burgas.backendserver.dto.VideoResponse;
import org.burgas.backendserver.exception.WrongFileFormatException;
import org.burgas.backendserver.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

import static java.net.URI.create;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static org.burgas.backendserver.message.VideoMessage.FILE_IS_EMPTY;
import static org.burgas.backendserver.message.VideoMessage.WRONG_FILE_FORMAT;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.*;
import static org.springframework.http.MediaTypeFactory.getMediaType;

@Controller
@RequestMapping("/videos")
@CrossOrigin(value = "http://localhost:4200")
public class VideoController {

    private static final Logger log = LoggerFactory.getLogger(VideoController.class);
    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping
    public @ResponseBody ResponseEntity<List<VideoResponse>> getAllVideos() {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(videoService.findAll());
    }

    @GetMapping(value = "/by-category")
    public @ResponseBody ResponseEntity<List<VideoResponse>> getVideosByCategoryId(@RequestParam Long categoryId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(videoService.findByCategoryId(categoryId));
    }

    @GetMapping(value = "/by-id")
    public @ResponseBody ResponseEntity<VideoResponse> getVideoById(@RequestParam Long videoId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(videoService.findById(videoId));
    }

    @GetMapping(value = "/stream/by-id")
    public @ResponseBody ResponseEntity<Resource> getVideoStreamById(
            @RequestParam Long videoId, @RequestHeader(required = false, value = "Range") String range
    ) {
        log.info("Header range: {}", range);
        VideoResponse videoResponse = videoService.findById(videoId);
        InputStreamResource inputStreamResource = new InputStreamResource(
                new ByteArrayInputStream(videoResponse.getData())
        );
        MediaType mediaType = getMediaType(inputStreamResource)
                .orElse(parseMediaType(videoResponse.getContentType()));
        return ResponseEntity
                .status(OK)
                .contentType(mediaType)
                .body(inputStreamResource);
    }

    @GetMapping(value = "/by-name")
    public @ResponseBody ResponseEntity<VideoResponse> getVideoByName(@RequestParam String name) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(videoService.findByName(name));
    }

    @GetMapping(value = "/stream/by-name")
    public @ResponseBody ResponseEntity<Resource> getVideoStreamByName(@RequestParam String name) {
        VideoResponse videoResponse = videoService.findByName(name);
        InputStreamResource inputStreamResource = new InputStreamResource(
                new ByteArrayInputStream(videoResponse.getData())
        );
        MediaType mediaType = getMediaType(inputStreamResource)
                .orElse(parseMediaType(videoResponse.getContentType()));
        return ResponseEntity
                .status(OK)
                .contentType(mediaType)
                .body(inputStreamResource);
    }

    @PostMapping(value = "/upload", consumes = MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody ResponseEntity<Long> uploadVideo(
            @RequestPart MultipartFile file, @RequestPart(required = false) String name,
            @RequestPart String streamerId, @RequestPart(required = false) String streamId,
            @RequestPart String categoryId, @RequestPart String description
    ) {
        if (
                file != null && !file.isEmpty() &&
                requireNonNull(file.getContentType())
                        .split("/")[0]
                        .equalsIgnoreCase("video")
        ) {
            Long strId = streamId == null ? null : Long.valueOf(streamId);
            VideoRequest videoRequest = new VideoRequest(
                    null, Long.valueOf(categoryId), Long.valueOf(streamerId), strId, name, description
            );
            Long videoId = videoService.uploadOrUpdate(videoRequest, file);
            return ResponseEntity
                    .status(FOUND)
                    .location(create("/videos/by-id?videoId=" + videoId))
                    .body(videoId);
        } else {
            throw new WrongFileFormatException(WRONG_FILE_FORMAT.getMessage() + " или " + FILE_IS_EMPTY.getMessage());
        }
    }

    @PostMapping(value = "/update")
    public @ResponseBody ResponseEntity<Long> updateVideo(
            @RequestBody VideoRequest videoRequest, @RequestParam Long streamerId
    ) {
        videoRequest.setStreamerId(streamerId);
        Long videoId = videoService.uploadOrUpdate(videoRequest, null);
        return ResponseEntity
                .status(FOUND)
                .location(create("/videos/by-id?videoId=" + videoId))
                .body(videoId);
    }

    @DeleteMapping(value = "/delete")
    public @ResponseBody ResponseEntity<String> deleteVideoById(@RequestParam Long videoId) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(videoService.deleteById(videoId));
    }
}
