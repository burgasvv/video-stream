package org.burgas.backendserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

@Entity
@IdClass(value = InvitedStreamerPK.class)
@SuppressWarnings(value = "unused")
public final class InvitedStreamer {

    @Id private long streamId;
    @Id private long streamerId;

    public long getStreamId() {
        return streamId;
    }

    public void setStreamId(long streamId) {
        this.streamId = streamId;
    }

    public long getStreamerId() {
        return streamerId;
    }

    public void setStreamerId(long streamerId) {
        this.streamerId = streamerId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final InvitedStreamer invitedStreamer;

        public Builder() {
            invitedStreamer = new InvitedStreamer();
        }

        public Builder streamId(Long streamId) {
            this.invitedStreamer.streamId = streamId;
            return this;
        }

        public Builder streamerId(Long streamerId) {
            this.invitedStreamer.streamerId = streamerId;
            return this;
        }

        public InvitedStreamer build() {
            return invitedStreamer;
        }
    }
}
