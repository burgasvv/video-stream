package org.burgas.backendserver.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.burgas.backendserver.dto.IdentityPrincipal;
import org.burgas.backendserver.entity.Streamer;
import org.burgas.backendserver.exception.IdentityNotAuthorizedException;
import org.burgas.backendserver.handler.RestClientHandler;
import org.burgas.backendserver.repository.StreamerRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.burgas.backendserver.entity.IdentityMessage.IDENTITY_NOT_AUTHENTICATED;
import static org.burgas.backendserver.entity.IdentityMessage.IDENTITY_NOT_AUTHORIZED;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@WebFilter(
        urlPatterns = "/videos/upload"
)
public class UploadVideoFilter extends OncePerRequestFilter {

    private final RestClientHandler restClientHandler;
    private final StreamerRepository streamerRepository;

    public UploadVideoFilter(RestClientHandler restClientHandler, StreamerRepository streamerRepository) {
        this.restClientHandler = restClientHandler;
        this.streamerRepository = streamerRepository;
    }

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authentication = request.getHeader(AUTHORIZATION);
        IdentityPrincipal identityPrincipal = this.restClientHandler.getIdentityPrincipal(authentication).getBody();

        if (identityPrincipal != null && identityPrincipal.getAuthenticated()) {
            byte[] streamerIdBytes = request.getPart("streamerId").getInputStream().readAllBytes();
            String streamerIdString = new String(streamerIdBytes, UTF_8);
            Long streamerId = Long.parseLong(streamerIdString.isBlank() ? "0" : streamerIdString);

            Streamer streamer = this.streamerRepository
                    .findById(streamerId)
                    .orElse(null);

            if (streamer != null && streamer.getIdentityId().equals(identityPrincipal.getId())) {
                filterChain.doFilter(request, response);

            } else {
                throw new IdentityNotAuthorizedException(IDENTITY_NOT_AUTHORIZED.getMessage());
            }

        } else {
            throw new IdentityNotAuthorizedException(IDENTITY_NOT_AUTHENTICATED.getMessage());
        }
    }
}
