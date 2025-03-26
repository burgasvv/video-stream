package org.burgas.backendserver.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.burgas.backendserver.dto.IdentityResponse;
import org.burgas.backendserver.entity.Streamer;
import org.burgas.backendserver.exception.IdentityNotAuthorizedException;
import org.burgas.backendserver.exception.NoLiveStreamException;
import org.burgas.backendserver.repository.StreamRepository;
import org.burgas.backendserver.repository.StreamerRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.burgas.backendserver.message.IdentityMessage.IDENTITY_NOT_AUTHENTICATED;
import static org.burgas.backendserver.message.IdentityMessage.IDENTITY_NOT_AUTHORIZED;
import static org.burgas.backendserver.message.StreamerMessage.NO_LIVE_STREAM;

@WebFilter(urlPatterns = "/invitations/send")
public class SendInvitationFilter extends OncePerRequestFilter {

    private final StreamerRepository streamerRepository;
    private final StreamRepository streamRepository;

    public SendInvitationFilter(
            StreamerRepository streamerRepository, StreamRepository streamRepository
    ) {
        this.streamerRepository = streamerRepository;
        this.streamRepository = streamRepository;
    }

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        String senderIdParam = request.getParameter("senderId");
        Authentication authentication = (Authentication) request.getUserPrincipal();

        if (authentication.isAuthenticated()) {
            IdentityResponse identityResponse = (IdentityResponse) authentication.getPrincipal();
            Long senderId = Long.parseLong(senderIdParam == null || senderIdParam.isBlank() ? "0" : senderIdParam);
            Streamer streamer = this.streamerRepository.findById(senderId).orElse(null);

            if (streamer != null && identityResponse.getId().equals(streamer.getIdentityId())) {

                if (this.streamRepository.existsStreamByStreamerIdAndIsLive(streamer.getId(), true)) {
                    filterChain.doFilter(request, response);

                } else {
                    throw new NoLiveStreamException(NO_LIVE_STREAM.getMessage());
                }

            } else {
                throw new IdentityNotAuthorizedException(IDENTITY_NOT_AUTHORIZED.getMessage());
            }

        } else {
            throw new IdentityNotAuthorizedException(IDENTITY_NOT_AUTHENTICATED.getMessage());
        }
    }
}
