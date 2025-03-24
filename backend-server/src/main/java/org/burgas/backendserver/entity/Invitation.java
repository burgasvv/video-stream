package org.burgas.backendserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.UUID;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuppressWarnings(value = "unused")
public final class Invitation {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long streamId;
    private Long senderId;
    private Long receiverId;
    private UUID streamKey;
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

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public UUID getStreamKey() {
        return streamKey;
    }

    public void setStreamKey(UUID streamKey) {
        this.streamKey = streamKey;
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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Invitation invitation;

        public Builder() {
            invitation = new Invitation();
        }

        public Builder id(Long id) {
            this.invitation.id = id;
            return this;
        }

        public Builder streamId(Long streamId) {
            this.invitation.streamId = streamId;
            return this;
        }

        public Builder senderId(Long senderId) {
            this.invitation.senderId = senderId;
            return this;
        }

        public Builder receiverId(Long receiverId) {
            this.invitation.receiverId = receiverId;
            return this;
        }

        public Builder streamKey(UUID streamKey) {
            this.invitation.streamKey = streamKey;
            return this;
        }

        public Builder accept(Boolean accept) {
            this.invitation.accept = accept;
            return this;
        }

        public Builder decline(Boolean decline) {
            this.invitation.decline = decline;
            return this;
        }

        public Invitation build() {
            return invitation;
        }
    }
}
