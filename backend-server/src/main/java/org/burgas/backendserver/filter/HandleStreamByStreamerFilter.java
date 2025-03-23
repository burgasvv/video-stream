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

import static org.burgas.backendserver.message.IdentityMessage.IDENTITY_NOT_AUTHENTICATED;
import static org.burgas.backendserver.message.IdentityMessage.IDENTITY_NOT_AUTHORIZED;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@WebFilter(
        urlPatterns = {
                "/streams/all/by-streamer",
                "/streams/start",
                "/streams/update",
                "/streams/stop"
        }
)
public class HandleStreamByStreamerFilter extends OncePerRequestFilter {

    private final StreamerRepository streamerRepository;
    private final RestClientHandler restClientHandler;

    public HandleStreamByStreamerFilter(StreamerRepository streamerRepository, RestClientHandler restClientHandler) {
        this.streamerRepository = streamerRepository;
        this.restClientHandler = restClientHandler;
    }

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authentication = request.getHeader(AUTHORIZATION);
        String streamerIdParam = request.getParameter("streamerId");
        Long streamerId = Long.parseLong(streamerIdParam == null || streamerIdParam.isEmpty() ? "0" : streamerIdParam);
        IdentityPrincipal identityPrincipal = this.restClientHandler.getIdentityPrincipal(authentication).getBody();

        if (identityPrincipal != null && identityPrincipal.getAuthenticated()) {
            Streamer streamer = this.streamerRepository.findById(streamerId).orElse(null);

            if (streamer != null && identityPrincipal.getId().equals(streamer.getIdentityId())) {
                filterChain.doFilter(request, response);

            } else {
                throw new IdentityNotAuthorizedException(IDENTITY_NOT_AUTHORIZED.getMessage());
            }

        } else {
            throw new IdentityNotAuthorizedException(IDENTITY_NOT_AUTHENTICATED.getMessage());
        }

    }
}
