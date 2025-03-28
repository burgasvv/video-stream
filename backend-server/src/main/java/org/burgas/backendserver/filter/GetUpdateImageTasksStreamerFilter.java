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
                "/streamers/by-id","/streamers/update", "/streamers/async/update",
                "/streamers/upload-set-image","/streamers/change-set-image",
                "/streamers/delete-image",
                "/streamers/add-categories", "/streamers/async/add-categories"
        }
)
public class GetUpdateImageTasksStreamerFilter extends OncePerRequestFilter {

    private final StreamerRepository streamerRepository;

    public GetUpdateImageTasksStreamerFilter(StreamerRepository streamerRepository) {
        this.streamerRepository = streamerRepository;
    }

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        String streamerId = request.getParameter("streamerId");
        Authentication authentication = (Authentication) request.getUserPrincipal();

        if (authentication.isAuthenticated()) {
            IdentityResponse identityResponse = (IdentityResponse) authentication.getPrincipal();

            Long identityId = identityResponse.getId();
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
