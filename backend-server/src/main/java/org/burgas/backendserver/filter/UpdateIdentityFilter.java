package org.burgas.backendserver.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.burgas.backendserver.dto.IdentityResponse;
import org.burgas.backendserver.entity.Identity;
import org.burgas.backendserver.exception.IdentityNotAuthorizedException;
import org.burgas.backendserver.repository.IdentityRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.burgas.backendserver.message.IdentityMessage.IDENTITY_NOT_AUTHENTICATED;
import static org.burgas.backendserver.message.IdentityMessage.IDENTITY_NOT_AUTHORIZED;

@WebFilter(urlPatterns = {"/identities/update", "/identities/async/update"})
public class UpdateIdentityFilter extends OncePerRequestFilter {

    private final IdentityRepository identityRepository;

    public UpdateIdentityFilter(IdentityRepository identityRepository) {
        this.identityRepository = identityRepository;
    }

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        String identityId = request.getParameter("identityId");
        Authentication authentication = (Authentication) request.getUserPrincipal();
        IdentityResponse identityResponse = (IdentityResponse) authentication.getPrincipal();

        if (authentication.isAuthenticated()) {
            Identity identity = identityRepository.findById(identityResponse.getId()).orElse(null);

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
