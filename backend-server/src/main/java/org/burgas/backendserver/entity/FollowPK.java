package org.burgas.backendserver.entity;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
@SuppressWarnings(value = "unused")
public final class FollowPK {

    private Long streamerId;
    private Long followerId;

    public Long getStreamerId() {
        return streamerId;
    }

    public Long getFollowerId() {
        return followerId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FollowPK that = (FollowPK) o;
        return Objects.equals(streamerId, that.streamerId) && Objects.equals(followerId, that.followerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(streamerId, followerId);
    }
}
