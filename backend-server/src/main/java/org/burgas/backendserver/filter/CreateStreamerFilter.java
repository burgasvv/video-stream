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
                "/streamers/create", "/streamers/async/create"
        }
)
public class CreateStreamerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        String identityId = request.getParameter("identityId");
        Authentication authentication = (Authentication) request.getUserPrincipal();

        if (authentication.isAuthenticated()) {
            IdentityResponse identityResponse = (IdentityResponse) authentication.getPrincipal();

            if (identityResponse.getId().equals(Long.parseLong(identityId == null || identityId.isBlank() ? "0" : identityId))) {
                filterChain.doFilter(request, response);
            }

        } else {
            throw new IdentityNotAuthorizedException(IDENTITY_NOT_AUTHORIZED.getMessage());
        }
    }
}
