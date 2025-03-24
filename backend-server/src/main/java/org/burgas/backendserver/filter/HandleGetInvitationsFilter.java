package org.burgas.backendserver.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.burgas.backendserver.dto.IdentityPrincipal;
import org.burgas.backendserver.entity.Streamer;
import org.burgas.backendserver.exception.IdentityNotAuthorizedException;
import org.burgas.backendserver.handler.RestClientHandler;
import org.burgas.backendserver.repository.StreamerRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.burgas.backendserver.message.IdentityMessage.IDENTITY_NOT_AUTHENTICATED;
import static org.burgas.backendserver.message.IdentityMessage.IDENTITY_NOT_AUTHORIZED;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@WebFilter(
        urlPatterns = {
                "/invitations/all/by-sender",
                "/invitations/all/by-receiver"
        }
)
public class HandleGetInvitationsFilter extends OncePerRequestFilter {

    private final RestClientHandler restClientHandler;
    private final StreamerRepository streamerRepository;

    public HandleGetInvitationsFilter(RestClientHandler restClientHandler, StreamerRepository streamerRepository) {
        this.restClientHandler = restClientHandler;
        this.streamerRepository = streamerRepository;
    }

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        long streamerId;
        if (request.getRequestURI().equals("/invitations/all/by-sender")) {
            String senderIdParam = request.getParameter("senderId");
            streamerId = Long.parseLong(senderIdParam == null || senderIdParam.isBlank() ? "0" : senderIdParam);
        } else {
            String senderIdParam = request.getParameter("receiverId");
            streamerId = Long.parseLong(senderIdParam == null || senderIdParam.isBlank() ? "0" : senderIdParam);
        }

        String authentication = request.getHeader(AUTHORIZATION);
        IdentityPrincipal identityPrincipal = this.restClientHandler.getIdentityPrincipal(authentication).getBody();

        if (identityPrincipal != null && identityPrincipal.getAuthenticated()) {
            Streamer streamer = this.streamerRepository.findById(streamerId).orElse(null);

            if (streamer != null && streamer.getIdentityId().equals(identityPrincipal.getId())) {
                filterChain.doFilter(request,response);

            } else {
                throw new IdentityNotAuthorizedException(IDENTITY_NOT_AUTHORIZED.getMessage());
            }

        } else {
            throw new IdentityNotAuthorizedException(IDENTITY_NOT_AUTHENTICATED.getMessage());
        }
    }
}
