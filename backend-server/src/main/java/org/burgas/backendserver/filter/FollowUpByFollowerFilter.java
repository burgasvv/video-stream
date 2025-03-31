package org.burgas.backendserver.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.burgas.backendserver.dto.IdentityResponse;
import org.burgas.backendserver.exception.IdentityNotAuthorizedException;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.burgas.backendserver.message.IdentityMessage.IDENTITY_NOT_AUTHENTICATED;
import static org.burgas.backendserver.message.IdentityMessage.IDENTITY_NOT_AUTHORIZED;

@WebFilter(
        urlPatterns = {
                "/follows/by-follower",
                "/follows/by-follower/sse",
                "/follows/by-follower/stream",
                "/follows/by-follower/secured",
                "/follows/by-follower/sse/secured",
                "/follows/by-follower/stream/secured",
        },
        asyncSupported = true
)
public class FollowUpByFollowerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        String followerIdParam = request.getParameter("followerId");
        Authentication authentication = (Authentication) request.getUserPrincipal();

        if (authentication.isAuthenticated()) {
            IdentityResponse identityResponse = (IdentityResponse) authentication.getPrincipal();
            Long followerId = Long.parseLong(followerIdParam == null || followerIdParam.isBlank() ? "0" : followerIdParam);

            if (identityResponse.getId().equals(followerId)) {
                filterChain.doFilter(request, response);

            } else {
                throw new IdentityNotAuthorizedException(IDENTITY_NOT_AUTHORIZED.getMessage());
            }

        } else {
            throw new IdentityNotAuthorizedException(IDENTITY_NOT_AUTHENTICATED.getMessage());
        }
    }
}
