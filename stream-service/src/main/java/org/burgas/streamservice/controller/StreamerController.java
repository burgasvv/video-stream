package org.burgas.streamservice.controller;

import org.burgas.streamservice.dto.StreamerRequest;
import org.burgas.streamservice.dto.StreamerResponse;
import org.burgas.streamservice.service.StreamerService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.net.URI.create;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Controller
@RequestMapping("/streamers")
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

    @GetMapping(value = "/by-id")
    public @ResponseBody ResponseEntity<StreamerResponse> getStreamerById(@RequestParam Long streamerId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(streamerService.findById(streamerId));
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
}
