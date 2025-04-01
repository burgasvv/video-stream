package org.burgas.backendserver.controller;

import org.burgas.backendserver.dto.IdentityResponse;
import org.burgas.backendserver.dto.StreamerResponse;
import org.burgas.backendserver.dto.SubscriptionRequest;
import org.burgas.backendserver.dto.SubscriptionResponse;
import org.burgas.backendserver.service.IdentityService;
import org.burgas.backendserver.service.StreamerService;
import org.burgas.backendserver.service.SubscriptionService;
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
@RequestMapping(value = "/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final IdentityService identityService;
    private final StreamerService streamerService;

    public SubscriptionController(
            SubscriptionService subscriptionService,
            IdentityService identityService, StreamerService streamerService
    ) {
        this.subscriptionService = subscriptionService;
        this.identityService = identityService;
        this.streamerService = streamerService;
    }

    @GetMapping(value = "/by-streamer")
    public @ResponseBody ResponseEntity<List<IdentityResponse>> getSubscribersByStreamer(
            @RequestParam final Long streamerId
    ) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.identityService.findSubscribersByStreamerId(streamerId));
    }

    @GetMapping(value = "/by-streamer/sse")
    public @ResponseBody ResponseEntity<SseEmitter> getSubscribersByStreamerSse(
            @RequestParam final Long streamerId
    ) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(APPLICATION_STREAM_JSON, UTF_8))
                .body(this.identityService.findSubscribersByStreamerIdSse(streamerId));
    }

    @GetMapping(value = "/by-streamer/stream")
    public @ResponseBody ResponseEntity<StreamingResponseBody> getSubscribersByStreamerStream(
            @RequestParam final Long streamerId
    ) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(APPLICATION_STREAM_JSON, UTF_8))
                .body(this.identityService.findSubscriberByStreamerIdStream(streamerId));
    }

    @GetMapping(value = "/by-subscriber")
    public @ResponseBody ResponseEntity<List<StreamerResponse>> getStreamersBySubscriber(
            @RequestParam final Long subscriberId
    ) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.streamerService.findStreamersBySubscriberId(subscriberId));
    }

    @GetMapping(value = "/by-subscriber/sse")
    public @ResponseBody ResponseEntity<SseEmitter> getStreamersBySubscriberSse(
            @RequestParam final Long subscriberId
    ) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(APPLICATION_STREAM_JSON, UTF_8))
                .body(this.streamerService.findStreamersBySubscriberIdSse(subscriberId));
    }

    @GetMapping(value = "/by-subscriber/stream")
    public @ResponseBody ResponseEntity<StreamingResponseBody> getStreamersBySubscriberStream(
            @RequestParam final Long subscriberId
    ) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(APPLICATION_STREAM_JSON, UTF_8))
                .body(this.streamerService.findStreamersBySubscriberIdStream(subscriberId));
    }

    @GetMapping(value = "/by-streamer/secured")
    public @ResponseBody ResponseEntity<List<SubscriptionResponse>> getSubscriptionsByStreamer(
            @RequestParam final Long streamerId
    ) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.subscriptionService.findSubscriptionsByStreamerId(streamerId));
    }

    @GetMapping(value = "/by-streamer/sse/secured")
    public @ResponseBody ResponseEntity<SseEmitter> getSubscriptionsByStreamerSse(
            @RequestParam final Long streamerId
    ) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(APPLICATION_STREAM_JSON, UTF_8))
                .body(this.subscriptionService.findSubscriptionsByStreamerIdSse(streamerId));
    }

    @GetMapping(value = "/by-streamer/stream/secured")
    public @ResponseBody ResponseEntity<StreamingResponseBody> getSubscriptionsByStreamerStream(
            @RequestParam final Long streamerId
    ) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(APPLICATION_STREAM_JSON, UTF_8))
                .body(this.subscriptionService.findSubscriptionsByStreamerIdStream(streamerId));
    }

    @GetMapping(value = "/by-subscriber/secured")
    public @ResponseBody ResponseEntity<List<SubscriptionResponse>> getSubscriptionsBySubscriberSecured(
            @RequestParam final Long subscriberId
    ) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.subscriptionService.findSubscriptionsBySubscriberId(subscriberId));
    }

    @GetMapping(value = "/by-subscriber/sse/secured")
    public @ResponseBody ResponseEntity<SseEmitter> getSubscriptionsBySubscriberSse(
            @RequestParam final Long subscriberId
    ) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(APPLICATION_STREAM_JSON, UTF_8))
                .body(this.subscriptionService.findSubscriptionsBySubscriberIdSse(subscriberId));
    }

    @GetMapping(value = "/by-subscriber/stream/secured")
    public @ResponseBody ResponseEntity<StreamingResponseBody> getSubscriptionsBySubscriberStream(
            @RequestParam final Long subscriberId
    ) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(APPLICATION_STREAM_JSON, UTF_8))
                .body(this.subscriptionService.findSubscriptionsBySubscriberIdStream(subscriberId));
    }

    @PostMapping(value = "/subscribe")
    public @ResponseBody ResponseEntity<String> subscribeOrUpdateOnStreamer(
            @RequestBody final SubscriptionRequest subscriptionRequest,
            @RequestParam final Long streamerId, @RequestParam final Long subscriberId
    ) {
        subscriptionRequest.setStreamerId(streamerId);
        subscriptionRequest.setSubscriberId(subscriberId);
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.subscriptionService.subscribeOrUpdate(subscriptionRequest));
    }

    @DeleteMapping(value = "/unsubscribe")
    public @ResponseBody ResponseEntity<String> unsubscribeOnStreamer(
            @RequestParam final Long streamerId, @RequestParam final Long subscriberId
    ) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.subscriptionService.unsubscribeAndDelete(streamerId, subscriberId));
    }
}
