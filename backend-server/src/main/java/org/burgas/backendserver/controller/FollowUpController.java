package org.burgas.backendserver.controller;

import org.burgas.backendserver.dto.FollowUpResponse;
import org.burgas.backendserver.dto.IdentityResponse;
import org.burgas.backendserver.dto.StreamerResponse;
import org.burgas.backendserver.service.FollowUpService;
import org.burgas.backendserver.service.IdentityService;
import org.burgas.backendserver.service.StreamerService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/follows")
public class FollowUpController {

    private final FollowUpService followUpService;
    private final IdentityService identityService;
    private final StreamerService streamerService;

    public FollowUpController(
            FollowUpService followUpService,
            IdentityService identityService, StreamerService streamerService
    ) {
        this.followUpService = followUpService;
        this.identityService = identityService;
        this.streamerService = streamerService;
    }

    @GetMapping(value = "/by-streamer")
    public @ResponseBody ResponseEntity<List<IdentityResponse>> getFollowersByStreamer(
            @RequestParam final Long streamerId
    ) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.identityService.findFollowersByStreamerId(streamerId));
    }

    @GetMapping(value = "/by-streamer/sse")
    public @ResponseBody ResponseEntity<SseEmitter> getFollowersByStreamerSse(
            @RequestParam final Long streamerId
    ) {
        //noinspection deprecation
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(APPLICATION_STREAM_JSON, UTF_8))
                .body(this.identityService.findFollowersByStreamerIdSse(streamerId));
    }

    @GetMapping(value = "/by-streamer/stream")
    public @ResponseBody ResponseEntity<StreamingResponseBody> getFollowersByStreamerStream(
            @RequestParam final Long streamerId
    ) {
        //noinspection deprecation
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(APPLICATION_STREAM_JSON, UTF_8))
                .body(this.identityService.findFollowersByStreamerIdStream(streamerId));
    }

    @GetMapping(value = "/by-follower")
    public @ResponseBody ResponseEntity<List<StreamerResponse>> getStreamersByFollower(
            @RequestParam final Long followerId
    ) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.streamerService.findStreamersByFollowerId(followerId));
    }

    @GetMapping(value = "/by-follower/sse")
    public @ResponseBody ResponseEntity<SseEmitter> getStreamersByFollowerSse(
            @RequestParam final Long followerId
    ) {
        //noinspection deprecation
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(APPLICATION_STREAM_JSON, UTF_8))
                .body(this.streamerService.findStreamersByFollowerIdSse(followerId));
    }

    @GetMapping(value = "/by-follower/stream")
    public @ResponseBody ResponseEntity<StreamingResponseBody> getStreamersByFollowerIdStream(
            @RequestParam final Long followerId
    ) {
        //noinspection deprecation
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(APPLICATION_STREAM_JSON, UTF_8))
                .body(this.streamerService.findStreamersByFollowerIdStream(followerId));
    }

    @GetMapping(value = "/by-streamer/secured")
    public @ResponseBody ResponseEntity<List<FollowUpResponse>> getFollowersByStreamerSecured(
            @RequestParam final Long streamerId
    ) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.followUpService.findAllByStreamerId(streamerId));
    }

    @GetMapping(value = "/by-streamer/sse/secured")
    public @ResponseBody ResponseEntity<SseEmitter> getAllFollowersByStreamerSse(
            @RequestParam final Long streamerId
    ) {
        //noinspection deprecation
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(APPLICATION_STREAM_JSON, UTF_8))
                .body(this.followUpService.findAllByStreamerIdSse(streamerId));
    }

    @GetMapping(value = "/by-streamer/stream/secured")
    public @ResponseBody ResponseEntity<StreamingResponseBody> getAllFollowersByStreamerStream(
            @RequestParam final Long streamerId
    ) {
        //noinspection deprecation
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(APPLICATION_STREAM_JSON, UTF_8))
                .body(this.followUpService.findAllByStreamerIdStream(streamerId));
    }

    @GetMapping(value = "/by-follower/secured")
    public @ResponseBody ResponseEntity<List<FollowUpResponse>> getAllStreamersByFollower(
            @RequestParam final Long followerId
    ) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.followUpService.findAllByFollowerId(followerId));
    }

    @GetMapping(value = "/by-follower/sse/secured")
    public @ResponseBody ResponseEntity<SseEmitter> getAllStreamersByFollowerSse(
            @RequestParam final Long followerId
    ) {
        //noinspection deprecation
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(APPLICATION_STREAM_JSON, UTF_8))
                .body(this.followUpService.findAllByFollowerIdSse(followerId));
    }

    @GetMapping(value = "/by-follower/stream/secured")
    public @ResponseBody ResponseEntity<StreamingResponseBody> getAllStreamersByFollowerStream(
            @RequestParam final Long followerId
    ) {
        //noinspection deprecation
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(APPLICATION_STREAM_JSON, UTF_8))
                .body(this.followUpService.findAllByFollowerIdStream(followerId));
    }

    @PostMapping(value = "/follow")
    public @ResponseBody ResponseEntity<String> followOnStreamer(
            @RequestParam final Long streamerId, @RequestParam final Long followerId
    ) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.followUpService.followOnStreamer(streamerId, followerId));
    }

    @DeleteMapping(value = "/unfollow")
    public @ResponseBody ResponseEntity<String> unfollowOnStreamer(
            @RequestParam final Long streamerId, @RequestParam final Long followerId
    ) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.followUpService.unfollowOnStreamer(streamerId, followerId));
    }
}
