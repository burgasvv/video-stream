package org.burgas.backendserver.service;

import org.burgas.backendserver.dto.InvitationAnswer;
import org.burgas.backendserver.dto.InvitationRequest;
import org.burgas.backendserver.dto.InvitationResponse;
import org.burgas.backendserver.exception.InvitationAlreadyHandledException;
import org.burgas.backendserver.exception.InvitationNotFoundException;
import org.burgas.backendserver.exception.StreamNotFoundException;
import org.burgas.backendserver.exception.WrongInvitationAnswerException;
import org.burgas.backendserver.mapper.InvitationMapper;
import org.burgas.backendserver.repository.InvitationRepository;
import org.burgas.backendserver.repository.StreamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.burgas.backendserver.message.InvitationMessage.*;
import static org.burgas.backendserver.message.StreamMessage.STREAM_NOT_FOUND;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final InvitationMapper invitationMapper;
    private final StreamRepository streamRepository;

    public InvitationService(
            InvitationRepository invitationRepository,
            InvitationMapper invitationMapper, StreamRepository streamRepository
    ) {
        this.invitationRepository = invitationRepository;
        this.invitationMapper = invitationMapper;
        this.streamRepository = streamRepository;
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
            isolation = REPEATABLE_READ, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String sendInvitation(final InvitationRequest invitationRequest) {
        this.invitationRepository
                .save(this.invitationMapper.toInvitation(invitationRequest));
        return INVITATION_WAS_SEND.getMessage();
    }

    @Transactional(
            isolation = REPEATABLE_READ, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String acceptOrDeclineInvitation(final InvitationAnswer invitationAnswer, final UUID streamKey) {
        return this.streamRepository
                .findById(invitationAnswer.getStreamId())
                .map(
                        stream -> {
                            if (invitationAnswer.getAccept() != null && invitationAnswer.getDecline() != null)
                                throw new InvitationAlreadyHandledException(INVITATION_ALREADY_HANDLED.getMessage());

                            if (stream.getSecured()) {

                                if (streamKey.equals(stream.getStreamKey())) {
                                    return handleInvitationAnswer(invitationAnswer);

                                } else {
                                    return WRONG_STREAM_KEY.getMessage();
                                }

                            } else {
                                return handleInvitationAnswer(invitationAnswer);
                            }
                        }
                )
                .orElseThrow(() -> new StreamNotFoundException(STREAM_NOT_FOUND.getMessage()));
    }

    private String handleInvitationAnswer(InvitationAnswer invitationAnswer) {
        return this.invitationRepository
                .findById(invitationAnswer.getId())
                .map(
                        invitation -> {
                            if (
                                    invitationAnswer.getAccept() == invitationAnswer.getDecline() ||
                                    invitationAnswer.getAccept() == null || invitationAnswer.getDecline() == null
                            )
                                throw new WrongInvitationAnswerException(WRONG_INVITATION_ANSWER.getMessage());

                            invitation.setAccept(invitationAnswer.getAccept());
                            invitation.setDecline(invitationAnswer.getDecline());
                            invitation = this.invitationRepository.save(invitation);

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
