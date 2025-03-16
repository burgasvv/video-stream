package org.burgas.backendserver.handler;

import org.burgas.backendserver.dto.IdentityPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

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
                .accept(APPLICATION_JSON)
                .header(AUTHORIZATION, authentication)
                .retrieve()
                .toEntity(IdentityPrincipal.class);
    }
}
