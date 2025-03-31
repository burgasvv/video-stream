package org.burgas.backendserver.entity;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
@SuppressWarnings("unused")
public final class SubscriptionPK {

    private Long streamerId;
    private Long subscriberId;

    public Long getStreamerId() {
        return streamerId;
    }

    public Long getSubscriberId() {
        return subscriberId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SubscriptionPK that = (SubscriptionPK) o;
        return Objects.equals(streamerId, that.streamerId) && Objects.equals(subscriberId, that.subscriberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(streamerId, subscriberId);
    }
}
