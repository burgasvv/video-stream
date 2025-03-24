package org.burgas.backendserver.dto;

@SuppressWarnings(value = "unused")
public final class InvitationAnswer {

    private Long id;
    private Long streamId;
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
