package org.burgas.backendserver.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import java.util.UUID;

@Entity
@IdClass(IdentityStreamerTokenPK.class)
@SuppressWarnings("unused")
public final class IdentityStreamerToken {

    @Id private Long identityId;
    @Id private Long streamerId;
    private UUID token;

    public Long getIdentityId() {
        return identityId;
    }

    public void setIdentityId(Long identityId) {
        this.identityId = identityId;
    }

    public Long getStreamerId() {
        return streamerId;
    }

    public void setStreamerId(Long streamerId) {
        this.streamerId = streamerId;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "IdentityStreamerToken{" +
               "identityId=" + identityId +
               ", streamerId=" + streamerId +
               ", token=" + token +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final IdentityStreamerToken identityStreamerToken;

        public Builder() {
            identityStreamerToken = new IdentityStreamerToken();
        }

        public Builder identityId(Long identityId) {
            this.identityStreamerToken.identityId = identityId;
            return this;
        }

        public Builder streamerId(Long streamerId) {
            this.identityStreamerToken.streamerId = streamerId;
            return this;
        }

        public Builder token(UUID token) {
            this.identityStreamerToken.token = token;
            return this;
        }

        public IdentityStreamerToken build() {
            return identityStreamerToken;
        }
    }
}
