package org.burgas.streamservice.handler;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.burgas.streamservice.dto.IdentityPrincipal;
import org.burgas.streamservice.dto.IdentityResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class RestClientHandler {

    private final RestClient restClient;

    public RestClientHandler(RestClient restClient) {
        this.restClient = restClient;
    }

    public ResponseEntity<IdentityPrincipal> getIdentityPrincipal(String authentication) {
        return restClient
                .get()
                .uri("http://localhost:8888/authentication/principal")
                .header(AUTHORIZATION, authentication)
                .retrieve()
                .toEntity(IdentityPrincipal.class);
    }

    @CircuitBreaker(
            name = "getIdentityResponseByIdentityStreamerToken",
            fallbackMethod = "fallBackGetIdentityResponseByIdentityStreamerToken"
    )
    public ResponseEntity<IdentityResponse> getIdentityResponseByIdentityStreamerToken(UUID token) {
        return restClient
                .get()
                .uri("http://localhost:8888/identities/by-identity-streamer-token/{token}", token)
                .accept(APPLICATION_JSON)
                .retrieve()
                .toEntity(IdentityResponse.class);
    }

    @SuppressWarnings("unused")
    public ResponseEntity<IdentityResponse> fallBackGetIdentityResponseByIdentityStreamerToken(Throwable throwable) {
        return ResponseEntity.ok(IdentityResponse.builder().build());
    }
}
