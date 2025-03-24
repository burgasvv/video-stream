package org.burgas.backendserver.entity;

import jakarta.persistence.Embeddable;

@Embeddable
@SuppressWarnings(value = "unused")
public final class InvitedStreamerPK {

    private Long streamId;
    private Long streamerId;

    public Long getStreamId() {
        return streamId;
    }

    public Long getStreamerId() {
        return streamerId;
    }
}
