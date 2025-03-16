package org.burgas.streamservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.burgas.streamservice.dto.IdentityPrincipal;
import org.burgas.streamservice.handler.RestClientHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@WebFilter(
        urlPatterns = {"/streamers/create"}
)
public class CreateStreamerFilter extends OncePerRequestFilter {

    private final RestClientHandler restClientHandler;

    public CreateStreamerFilter(RestClientHandler restClientHandler) {
        this.restClientHandler = restClientHandler;
    }

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authentication = request.getHeader(AUTHORIZATION);
        String identityId = request.getParameter("identityId");
        IdentityPrincipal identityPrincipal = restClientHandler.getIdentityPrincipal(authentication).getBody();

        if (identityPrincipal != null && identityPrincipal.getAuthenticated()) {

            if (identityPrincipal.getId().equals(Long.parseLong(identityId == null || identityId.isBlank() ? "0" : identityId))) {
                filterChain.doFilter(request, response);
            }

        } else {
            throw new RuntimeException("Пользователь не авторизован и не аутентифицирован");
        }
    }
}
