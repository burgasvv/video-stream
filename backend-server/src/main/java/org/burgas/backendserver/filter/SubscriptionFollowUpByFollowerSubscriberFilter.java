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

                "/subscriptions/by-subscriber",
                "/subscriptions/by-subscriber/sse",
                "/subscriptions/by-subscriber/stream",
                "/subscriptions/by-subscriber/secured",
                "/subscriptions/by-subscriber/sse/secured",
                "/subscriptions/by-subscriber/stream/secured",
        },
        asyncSupported = true
)
public class SubscriptionFollowUpByFollowerSubscriberFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        String identityId;
        if (request.getRequestURI().startsWith("/subscriptions/by-subscriber")) {
            identityId = request.getParameter("subscriberId");

        } else {
            identityId = request.getParameter("followerId");
        }

        Authentication authentication = (Authentication) request.getUserPrincipal();

        if (authentication.isAuthenticated()) {
            IdentityResponse identityResponse = (IdentityResponse) authentication.getPrincipal();
            Long followerId = Long.parseLong(identityId == null || identityId.isBlank() ? "0" : identityId);

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
