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

import static org.burgas.backendserver.entity.IdentityMessage.IDENTITY_NOT_AUTHENTICATED;
import static org.burgas.backendserver.entity.IdentityMessage.IDENTITY_NOT_AUTHORIZED;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@WebFilter(
        urlPatterns = {"/streamers/by-id","/streamers/update"}
)
public class GetAndUpdateStreamerFilter extends OncePerRequestFilter {

    private final RestClientHandler restClientHandler;
    private final StreamerRepository streamerRepository;

    public GetAndUpdateStreamerFilter(RestClientHandler restClientHandler, StreamerRepository streamerRepository) {
        this.restClientHandler = restClientHandler;
        this.streamerRepository = streamerRepository;
    }

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authentication = request.getHeader(AUTHORIZATION);
        String streamerId = request.getParameter("streamerId");
        IdentityPrincipal identityPrincipal = this.restClientHandler.getIdentityPrincipal(authentication).getBody();

        if (identityPrincipal != null && identityPrincipal.getAuthenticated()) {

            Long identityId = identityPrincipal.getId();
            Long obj = Long.parseLong(streamerId == null || streamerId.isBlank() ? "0" : streamerId);
            Streamer streamer = this.streamerRepository.findById(obj).orElse(null);

            if (streamer != null && streamer.getIdentityId().equals(identityId == null ? 0L : identityId)) {
                filterChain.doFilter(request, response);

            } else {
                throw new IdentityNotAuthorizedException(IDENTITY_NOT_AUTHORIZED.getMessage());
            }

        } else {
            throw new IdentityNotAuthorizedException(IDENTITY_NOT_AUTHENTICATED.getMessage());
        }
    }
}
