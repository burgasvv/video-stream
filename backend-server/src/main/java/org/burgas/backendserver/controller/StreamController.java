package org.burgas.backendserver.controller;

import org.burgas.backendserver.dto.StreamResponse;
import org.burgas.backendserver.service.StreamService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Controller
@RequestMapping(value = "/streams")
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
}
