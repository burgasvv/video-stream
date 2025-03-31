package org.burgas.backendserver.dto;

import org.springframework.stereotype.Component;

@Component
@SuppressWarnings(value = "unused")
public final class InvitationAnswer {

    private Long id;
    private Long streamId;
    private Long invitedId;
    private Boolean accept;
    private Boolean decline;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStreamId() {
        return streamId;
    }

    public void setStreamId(Long streamId) {
        this.streamId = streamId;
    }

    public Long getInvitedId() {
        return invitedId;
    }

    public void setInvitedId(Long invitedId) {
        this.invitedId = invitedId;
    }

    public Boolean getAccept() {
        return accept;
    }

    public void setAccept(Boolean accept) {
        this.accept = accept;
    }

    public Boolean getDecline() {
        return decline;
    }

    public void setDecline(Boolean decline) {
        this.decline = decline;
    }
}
