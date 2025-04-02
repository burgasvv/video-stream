package org.burgas.backendserver.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.*;

@Entity
@SuppressWarnings("unused")
public final class Subscription {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long streamerId;
    private Long subscriberId;
    private Long tariffId;
    private LocalDateTime subscribedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStreamerId() {
        return streamerId;
    }

    public void setStreamerId(Long streamerId) {
        this.streamerId = streamerId;
    }

    public Long getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(Long subscriberId) {
        this.subscriberId = subscriberId;
    }

    public Long getTariffId() {
        return tariffId;
    }

    public void setTariffId(Long tariffId) {
        this.tariffId = tariffId;
    }

    public LocalDateTime getSubscribedAt() {
        return subscribedAt;
    }

    public void setSubscribedAt(LocalDateTime subscribedAt) {
        this.subscribedAt = subscribedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Subscription subscription;

        public Builder() {
            subscription = new Subscription();
        }

        public Builder id(Long id) {
            this.subscription.id = id;
            return this;
        }

        public Builder streamerId(Long streamerId) {
            this.subscription.streamerId = streamerId;
            return this;
        }

        public Builder subscriberId(Long subscriberId) {
            this.subscription.subscriberId = subscriberId;
            return this;
        }

        public Builder tariffId(Long tariffId) {
            this.subscription.tariffId = tariffId;
            return this;
        }

        public Builder subscribedAt(LocalDateTime subscribedAt) {
            this.subscription.subscribedAt = subscribedAt;
            return this;
        }

        public Subscription build() {
            return this.subscription;
        }
    }
}
