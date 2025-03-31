package org.burgas.backendserver.service;

import org.burgas.backendserver.dto.InvitationAnswer;
import org.burgas.backendserver.dto.InvitationRequest;
import org.burgas.backendserver.dto.InvitationResponse;
import org.burgas.backendserver.entity.Invitation;
import org.burgas.backendserver.entity.InvitedStreamer;
import org.burgas.backendserver.entity.Stream;
import org.burgas.backendserver.exception.InvitationAlreadyHandledException;
import org.burgas.backendserver.exception.InvitationNotFoundException;
import org.burgas.backendserver.exception.StreamNotFoundException;
import org.burgas.backendserver.exception.WrongInvitationAnswerException;
import org.burgas.backendserver.listener.InvitationEvent;
import org.burgas.backendserver.mapper.InvitationMapper;
import org.burgas.backendserver.repository.InvitationRepository;
import org.burgas.backendserver.repository.InvitedStreamerRepository;
import org.burgas.backendserver.repository.StreamRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.burgas.backendserver.message.InvitationMessage.*;
import static org.burgas.backendserver.message.StreamMessage.STREAM_NOT_FOUND;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final InvitationMapper invitationMapper;
    private final StreamRepository streamRepository;
    private final InvitedStreamerRepository invitedStreamerRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public InvitationService(
            InvitationRepository invitationRepository,
            InvitationMapper invitationMapper, StreamRepository streamRepository,
            InvitedStreamerRepository invitedStreamerRepository, ApplicationEventPublisher applicationEventPublisher
    ) {
        this.invitationRepository = invitationRepository;
        this.invitationMapper = invitationMapper;
        this.streamRepository = streamRepository;
        this.invitedStreamerRepository = invitedStreamerRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public List<InvitationResponse> findBySenderId(final Long senderId) {
        return this.invitationRepository
                .findInvitationsBySenderId(senderId)
                .stream()
                .map(this.invitationMapper::toInvitationResponse)
                .toList();
    }

    public List<InvitationResponse> findByReceiverId(final Long receiverId) {
        return this.invitationRepository
                .findInvitationsByReceiverId(receiverId)
                .stream()
                .map(this.invitationMapper::toInvitationResponse)
                .toList();
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String sendInvitation(final InvitationRequest invitationRequest) {
        Invitation saved = this.invitationRepository
                .save(this.invitationMapper.toInvitation(invitationRequest));
        this.applicationEventPublisher.publishEvent(new InvitationEvent(saved));
        return INVITATION_WAS_SEND.getMessage();
    }

    @Transactional(
            isolation = SERIALIZABLE, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String acceptOrDeclineInvitation(
            final InvitationAnswer invitationAnswer, final UUID streamKey
    ) {
        if (
                invitationAnswer.getAccept() == invitationAnswer.getDecline() ||
                invitationAnswer.getAccept() == null || invitationAnswer.getDecline() == null
        )
            throw new WrongInvitationAnswerException(WRONG_INVITATION_ANSWER.getMessage());

        return this.streamRepository
                .findById(invitationAnswer.getStreamId())
                .map(
                        stream -> {
                            if (stream.getSecured()) {

                                if (invitationAnswer.getAccept()) {
                                    String testKey = streamKey == null  ? "" : streamKey.toString();

                                    if (testKey.equals(stream.getStreamKey().toString())) {
                                        return handleInvitationAnswer(invitationAnswer, stream);

                                    } else {
                                        return WRONG_STREAM_KEY.getMessage();
                                    }

                                } else if (invitationAnswer.getDecline()) {
                                    return handleInvitationAnswer(invitationAnswer, stream);

                                } else {
                                    throw new WrongInvitationAnswerException(HANDLE_INVITATION_ERROR.getMessage());
                                }

                            } else {
                                return handleInvitationAnswer(invitationAnswer, stream);
                            }
                        }
                )
                .orElseThrow(() -> new StreamNotFoundException(STREAM_NOT_FOUND.getMessage()));
    }

    private String handleInvitationAnswer(InvitationAnswer invitationAnswer, Stream stream) {
        return this.invitationRepository
                .findById(invitationAnswer.getId())
                .map(
                        invitation -> {
                            if (invitation.getAccept() != null && invitation.getDecline() != null)
                                throw new InvitationAlreadyHandledException(INVITATION_ALREADY_HANDLED.getMessage());

                            invitation.setAccept(invitationAnswer.getAccept());
                            invitation.setDecline(invitationAnswer.getDecline());
                            invitation = this.invitationRepository.save(invitation);

                            if (invitation.getAccept()) {
                                this.invitedStreamerRepository
                                        .save(
                                                InvitedStreamer.builder()
                                                        .streamId(stream.getId())
                                                        .streamerId(invitationAnswer.getInvitedId())
                                                        .build()
                                        );
                            }
                            this.applicationEventPublisher.publishEvent(new InvitationEvent(invitation));

                            return invitation.getAccept() && !invitation.getDecline() ?
                                    INVITATION_WAS_ACCEPTED.getMessage() :
                                    INVITATION_WAS_DECLINED.getMessage();
                        }
                )
                .orElseThrow(
                        () -> new InvitationNotFoundException(INVITATION_NOT_FOUND.getMessage())
                );
    }
}
