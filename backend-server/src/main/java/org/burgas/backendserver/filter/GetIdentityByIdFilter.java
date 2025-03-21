package org.burgas.backendserver.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.burgas.backendserver.dto.IdentityPrincipal;
import org.burgas.backendserver.exception.IdentityNotAuthorizedException;
import org.burgas.backendserver.handler.RestClientHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.burgas.backendserver.message.IdentityMessage.IDENTITY_NOT_AUTHORIZED;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@WebFilter(
        urlPatterns = {"/identities/by-id", "/identities/by-id/async"}
)
public class GetIdentityByIdFilter extends OncePerRequestFilter {

    private final RestClientHandler restClientHandler;

    public GetIdentityByIdFilter(RestClientHandler restClientHandler) {
        this.restClientHandler = restClientHandler;
    }

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authentication = request.getHeader(AUTHORIZATION);
        String identityId = request.getParameter("identityId");
        IdentityPrincipal identityPrincipal = restClientHandler.getIdentityPrincipal(authentication).getBody();

        if (
                identityPrincipal != null &&
                identityPrincipal.getAuthenticated() &&
                (identityPrincipal.getId().equals(identityId == null || identityId.isBlank() ? 0L : Long.parseLong(identityId)) ||
                identityPrincipal.getAuthority().equals("ROLE_ADMIN"))
        ) {
            filterChain.doFilter(request, response);

        } else {
            throw new IdentityNotAuthorizedException(IDENTITY_NOT_AUTHORIZED.getMessage());
        }
    }
}
