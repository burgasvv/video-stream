package org.burgas.backendserver.mapper;

import org.burgas.backendserver.dto.InvitationRequest;
import org.burgas.backendserver.dto.InvitationResponse;
import org.burgas.backendserver.entity.Invitation;
import org.burgas.backendserver.entity.Stream;
import org.burgas.backendserver.entity.Streamer;
import org.burgas.backendserver.exception.StreamNotFoundException;
import org.burgas.backendserver.repository.StreamRepository;
import org.burgas.backendserver.repository.StreamerRepository;
import org.springframework.stereotype.Component;

import static org.burgas.backendserver.message.StreamMessage.STREAM_NOT_FOUND;

@Component
public class InvitationMapper {

    private final StreamerRepository streamerRepository;
    private final StreamerMapper streamerMapper;
    private final StreamRepository streamRepository;

    public InvitationMapper(
            StreamerRepository streamerRepository,
            StreamerMapper streamerMapper, StreamRepository streamRepository
    ) {
        this.streamerRepository = streamerRepository;
        this.streamerMapper = streamerMapper;
        this.streamRepository = streamRepository;
    }

    private <T> T getData(T first, @SuppressWarnings("SameParameterValue") T second) {
        return first == null || first == "" ? second : first;
    }

    public Invitation toInvitation(final InvitationRequest invitationRequest) {
        Long streamId = getData(invitationRequest.getStreamId(), 0L);
        return this.streamRepository
                .findById(streamId)
                .map(
                        stream -> {
                            if (stream.getSecured()) {
                                return Invitation.builder()
                                        .streamId(stream.getId())
                                        .senderId(invitationRequest.getSenderId())
                                        .receiverId(invitationRequest.getReceiverId())
                                        .streamKey(stream.getStreamKey())
                                        .build();
                            } else {
                                return Invitation.builder()
                                        .streamId(stream.getId())
                                        .senderId(invitationRequest.getSenderId())
                                        .receiverId(invitationRequest.getReceiverId())
                                        .build();
                            }
                        }
                )
                .orElseThrow(() -> new StreamNotFoundException(STREAM_NOT_FOUND.getMessage()));
    }

    public InvitationResponse toInvitationResponse(final Invitation invitation) {
        return InvitationResponse.builder()
                .id(invitation.getId())
                .stream(this.streamRepository.findById(invitation.getStreamId()).orElseGet(Stream::new).getName())
                .sender(
                        this.streamerMapper
                                .toStreamerResponse(
                                        this.streamerRepository
                                                .findById(invitation.getSenderId())
                                                .orElseGet(Streamer::new)
                                )
                                .getIdentity()
                                .getNickname()
                )
                .receiver(
                        this.streamerMapper
                                .toStreamerResponse(
                                        this.streamerRepository
                                                .findById(invitation.getReceiverId())
                                                .orElseGet(Streamer::new)
                                )
                                .getIdentity()
                                .getNickname()
                )
                .streamKey(invitation.getStreamKey())
                .accept(invitation.getAccept())
                .build();
    }
}
