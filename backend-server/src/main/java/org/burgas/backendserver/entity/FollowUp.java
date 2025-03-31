package org.burgas.backendserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import java.time.LocalDateTime;

@Entity
@IdClass(value = FollowUpPK.class)
@SuppressWarnings(value = "unused")
public final class FollowUp {

    @Id private Long streamerId;
    @Id private Long followerId;
    private LocalDateTime followedAt;

    public Long getStreamerId() {
        return streamerId;
    }

    public void setStreamerId(Long streamerId) {
        this.streamerId = streamerId;
    }

    public Long getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }

    public LocalDateTime getFollowedAt() {
        return followedAt;
    }

    public void setFollowedAt(LocalDateTime followedAt) {
        this.followedAt = followedAt;
    }

    @Override
    public String toString() {
        return "FollowUp{" +
               "streamerId=" + streamerId +
               ", followerId=" + followerId +
               ", followedAt=" + followedAt +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final FollowUp followUp;

        public Builder() {
            followUp = new FollowUp();
        }

        public Builder streamerId(Long streamerId) {
            this.followUp.streamerId = streamerId;
            return this;
        }

        public Builder followerId(Long followerId) {
            this.followUp.followerId = followerId;
            return this;
        }

        public Builder followedAt(LocalDateTime followedAt) {
            this.followUp.followedAt = followedAt;
            return this;
        }

        public FollowUp build() {
            return followUp;
        }
    }
}
