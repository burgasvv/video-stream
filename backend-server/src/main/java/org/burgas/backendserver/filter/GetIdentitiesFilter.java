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

import static org.burgas.backendserver.message.IdentityMessage.IDENTITY_NOT_AUTHORIZED;

@WebFilter(
        urlPatterns = {
                "/identities", "/identities/async",
                "/identities/sse", "/identities/stream"
        },
        asyncSupported = true
)
public class GetIdentitiesFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        Authentication authentication = (Authentication) request.getUserPrincipal();
        IdentityResponse identityResponse = (IdentityResponse) authentication.getPrincipal();

        if (
                authentication.isAuthenticated() &&
                identityResponse.getAuthority().getName().equalsIgnoreCase("ROLE_ADMIN")
        ) {

            filterChain.doFilter(request, response);

        } else {
            throw new IdentityNotAuthorizedException(IDENTITY_NOT_AUTHORIZED.getMessage());
        }
    }
}
