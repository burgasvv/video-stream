package org.burgas.streamservice.handler;

import org.burgas.streamservice.dto.IdentityResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class RestClientHandler {

    private final RestClient restClient;

    public RestClientHandler(RestClient restClient) {
        this.restClient = restClient;
    }

    public ResponseEntity<IdentityResponse> getIdentityResponseByIdentityStreamerToken(UUID token) {
        return restClient
                .get()
                .uri("http://localhost:8888/identities/by-identity-streamer-token/{token}", token)
                .accept(APPLICATION_JSON)
                .retrieve()
                .toEntity(IdentityResponse.class);
    }
}
