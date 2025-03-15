package org.burgas.identityserver.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.burgas.identityserver.dto.IdentityPrincipal;
import org.burgas.identityserver.entity.Identity;
import org.burgas.identityserver.exception.IdentityNotAuthorizedException;
import org.burgas.identityserver.handler.RestClientHandler;
import org.burgas.identityserver.repository.IdentityRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.burgas.identityserver.entity.IdentityMessage.IDENTITY_NOT_AUTHENTICATED;
import static org.burgas.identityserver.entity.IdentityMessage.IDENTITY_NOT_AUTHORIZED;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@WebFilter(urlPatterns = "/identities/update")
public class UpdateIdentityFilter extends OncePerRequestFilter {

    private final RestClientHandler restClientHandler;
    private final IdentityRepository identityRepository;

    public UpdateIdentityFilter(RestClientHandler restClientHandler, IdentityRepository identityRepository) {
        this.restClientHandler = restClientHandler;
        this.identityRepository = identityRepository;
    }

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authentication = request.getHeader(AUTHORIZATION);
        String identityId = request.getParameter("identityId");
        IdentityPrincipal identityPrincipal = restClientHandler.getIdentityPrincipal(authentication).getBody();

        if (identityPrincipal != null && identityPrincipal.getAuthenticated()) {
            Identity identity = identityRepository.findById(identityPrincipal.getId()).orElse(null);

            if (identity != null && identity.getId().equals(Long.parseLong(identityId == null || identityId.isBlank() ? "0" : identityId))) {
                filterChain.doFilter(request, response);

            } else {
                throw new IdentityNotAuthorizedException(IDENTITY_NOT_AUTHORIZED.getMessage());
            }

        } else {
            throw new IdentityNotAuthorizedException(IDENTITY_NOT_AUTHENTICATED.getMessage());
        }
    }
}
