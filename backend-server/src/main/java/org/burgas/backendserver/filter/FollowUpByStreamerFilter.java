package org.burgas.backendserver.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.burgas.backendserver.dto.IdentityResponse;
import org.burgas.backendserver.entity.Streamer;
import org.burgas.backendserver.exception.IdentityNotAuthorizedException;
import org.burgas.backendserver.repository.StreamerRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.burgas.backendserver.message.IdentityMessage.IDENTITY_NOT_AUTHENTICATED;
import static org.burgas.backendserver.message.IdentityMessage.IDENTITY_NOT_AUTHORIZED;

@WebFilter(
        urlPatterns = {
                "/follows/by-streamer",
                "/follows/by-streamer/sse",
                "/follows/by-streamer/stream",
                "/follows/by-streamer/secured",
                "/follows/by-streamer/sse/secured",
                "/follows/by-streamer/stream/secured",
        },
        asyncSupported = true
)
public class FollowUpByStreamerFilter extends OncePerRequestFilter {

    private final StreamerRepository streamerRepository;

    public FollowUpByStreamerFilter(StreamerRepository streamerRepository) {
        this.streamerRepository = streamerRepository;
    }

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        String streamerIdParam = request.getParameter("streamerId");
        Authentication authentication = (Authentication) request.getUserPrincipal();

        if (authentication.isAuthenticated()) {
            IdentityResponse identityResponse = (IdentityResponse) authentication.getPrincipal();
            Long streamerId = Long.parseLong(streamerIdParam == null || streamerIdParam.isBlank() ? "0" : streamerIdParam);
            Streamer streamer = this.streamerRepository.findById(streamerId).orElse(null);

            if (streamer != null && identityResponse.getId().equals(streamer.getIdentityId())) {
                filterChain.doFilter(request, response);

            } else {
                throw new IdentityNotAuthorizedException(IDENTITY_NOT_AUTHORIZED.getMessage());
            }

        } else {
            throw new IdentityNotAuthorizedException(IDENTITY_NOT_AUTHENTICATED.getMessage());
        }
    }
}
