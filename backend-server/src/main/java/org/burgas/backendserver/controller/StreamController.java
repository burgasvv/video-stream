package org.burgas.backendserver.controller;

import org.burgas.backendserver.dto.StreamRequest;
import org.burgas.backendserver.dto.StreamResponse;
import org.burgas.backendserver.service.StreamService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.net.URI.create;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Controller
@RequestMapping(value = "/streams")
@CrossOrigin(value = "http://localhost:4200")
public class StreamController {

    private final StreamService streamService;

    public StreamController(StreamService streamService) {
        this.streamService = streamService;
    }

    @GetMapping(value = "/all/by-streamer")
    public @ResponseBody ResponseEntity<List<StreamResponse>> getStreamsByStreamer(@RequestParam Long streamerId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.streamService.findByStreamerId(streamerId));
    }

    @GetMapping(value = "/by-id")
    public @ResponseBody ResponseEntity<StreamResponse> getStreamById(@RequestParam Long streamId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.streamService.findById(streamId));
    }

    @PostMapping(value = "/start")
    public @ResponseBody ResponseEntity<Long> startStream(
            @RequestBody StreamRequest streamRequest, @RequestParam Long streamerId
    ) {
        streamRequest.setStreamerId(streamerId);
        Long streamId = this.streamService.startUpdateOrStop(streamRequest);
        return ResponseEntity
                .status(FOUND)
                .location(create("/streams/by-id?streamId=" + streamId))
                .body(streamId);
    }

    @PostMapping(value = "/update")
    public @ResponseBody ResponseEntity<Long> updateStream(
            @RequestBody StreamRequest streamRequest, @RequestParam Long streamerId
    ) {
        streamRequest.setStreamerId(streamerId);
        Long streamId = this.streamService.startUpdateOrStop(streamRequest);
        return ResponseEntity
                .status(FOUND)
                .location(create("/streams/by-id?streamId=" + streamId))
                .body(streamId);
    }

    @PostMapping(value = "/stop")
    public @ResponseBody ResponseEntity<Long> stopStream(
            @RequestBody StreamRequest streamRequest, @RequestParam Long streamerId
    ) {
        streamRequest.setStreamerId(streamerId);
        Long streamId = this.streamService.startUpdateOrStop(streamRequest);
        return ResponseEntity
                .status(FOUND)
                .location(create("/streams/by-id?streamId=" + streamId))
                .body(streamId);
    }
}
