package org.burgas.identityserver.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.burgas.identityserver.dto.IdentityPrincipal;
import org.burgas.identityserver.exception.IdentityNotAuthorizedException;
import org.burgas.identityserver.handler.RestClientHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.burgas.identityserver.entity.IdentityMessage.IDENTITY_NOT_AUTHORIZED;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@WebFilter(urlPatterns = "/identities")
public class GetIdentitiesFilter extends OncePerRequestFilter {

    private final RestClientHandler restClientHandler;

    public GetIdentitiesFilter(RestClientHandler restClientHandler) {
        this.restClientHandler = restClientHandler;
    }

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authentication = request.getHeader(AUTHORIZATION);
        IdentityPrincipal identityPrincipal = restClientHandler.getIdentityPrincipal(authentication).getBody();

        if (
                identityPrincipal != null &&
                identityPrincipal.getAuthenticated() &&
                identityPrincipal.getAuthority().equalsIgnoreCase("ROLE_ADMIN")
        ) {
            filterChain.doFilter(request, response);

        } else {
            throw new IdentityNotAuthorizedException(IDENTITY_NOT_AUTHORIZED.getMessage());
        }
    }
}
