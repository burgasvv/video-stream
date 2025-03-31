package org.burgas.backendserver.entity;

import jakarta.persistence.Embeddable;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InvitedStreamerPK that = (InvitedStreamerPK) o;
        return Objects.equals(streamId, that.streamId) && Objects.equals(streamerId, that.streamerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(streamId, streamerId);
    }
}
