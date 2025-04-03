package org.burgas.backendserver.dto;

import java.util.Objects;
import java.util.UUID;

@SuppressWarnings(value = "unused")
public final class InvitationResponse {

    private Long id;
    private String stream;
    private String sender;
    private String receiver;
    private UUID streamKey;
    private Boolean accept;
    private Boolean decline;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InvitationResponse that = (InvitationResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(stream, that.stream)
               && Objects.equals(sender, that.sender) && Objects.equals(receiver, that.receiver)
               && Objects.equals(streamKey, that.streamKey) && Objects.equals(accept, that.accept)
               && Objects.equals(decline, that.decline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stream, sender, receiver, streamKey, accept, decline);
    }

    @Override
    public String toString() {
        return "InvitationResponse{" +
               "id=" + id +
               ", stream='" + stream + '\'' +
               ", sender='" + sender + '\'' +
               ", receiver='" + receiver + '\'' +
               ", streamKey=" + streamKey +
               ", accept=" + accept +
               ", decline=" + decline +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final InvitationResponse invitationResponse;

        public Builder() {
            invitationResponse = new InvitationResponse();
        }

        public Builder id(Long id) {
            this.invitationResponse.id = id;
            return this;
        }

        public Builder stream(String stream) {
            this.invitationResponse.stream = stream;
            return this;
        }

        public Builder sender(String sender) {
            this.invitationResponse.sender = sender;
            return this;
        }

        public Builder receiver(String receiver) {
            this.invitationResponse.receiver = receiver;
            return this;
        }

        public Builder streamKey(UUID streamKey) {
            this.invitationResponse.streamKey = streamKey;
            return this;
        }

        public Builder accept(Boolean accept) {
            this.invitationResponse.accept = accept;
            return this;
        }

        public Builder decline(Boolean decline) {
            this.invitationResponse.decline = decline;
            return this;
        }

        public InvitationResponse build() {
            return invitationResponse;
        }
    }
}
