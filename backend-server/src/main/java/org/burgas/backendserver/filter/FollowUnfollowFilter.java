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

@WebFilter(urlPatterns = {"/follows/follow", "/follows/unfollow"})
public class FollowUnfollowFilter extends OncePerRequestFilter {

    private final StreamerRepository streamerRepository;

    public FollowUnfollowFilter(StreamerRepository streamerRepository) {
        this.streamerRepository = streamerRepository;
    }

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        String streamerIdParam = request.getParameter("streamerId");
        String followerIdParam = request.getParameter("followerId");
        Authentication authentication = (Authentication) request.getUserPrincipal();

        if (authentication.isAuthenticated()) {
            Long streamerId = Long.parseLong(streamerIdParam == null || streamerIdParam.isBlank() ? "0" : streamerIdParam);
            Long followerId = Long.parseLong(followerIdParam == null || followerIdParam.isBlank() ? "0" : followerIdParam);
            Streamer streamer = this.streamerRepository.findById(streamerId).orElse(null);
            IdentityResponse identityResponse = (IdentityResponse) authentication.getPrincipal();

            if (streamer != null && identityResponse.getId().equals(followerId)) {
                filterChain.doFilter(request, response);

            } else {
                throw new IdentityNotAuthorizedException(IDENTITY_NOT_AUTHORIZED.getMessage());
            }

        } else {
            throw new IdentityNotAuthorizedException(IDENTITY_NOT_AUTHENTICATED.getMessage());
        }
    }
}
