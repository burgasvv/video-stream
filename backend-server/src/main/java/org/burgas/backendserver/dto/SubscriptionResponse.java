package org.burgas.backendserver.dto;

import org.burgas.backendserver.entity.Tariff;

@SuppressWarnings("unused")
public final class SubscriptionResponse {

    private StreamerResponse streamer;
    private IdentityResponse subscriber;
    private Tariff tariff;
    private String subscribedAt;

    public StreamerResponse getStreamer() {
        return streamer;
    }

    public IdentityResponse getSubscriber() {
        return subscriber;
    }

    public Tariff getTariff() {
        return tariff;
    }

    public String getSubscribedAt() {
        return subscribedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final SubscriptionResponse subscriptionResponse;

        public Builder() {
            subscriptionResponse = new SubscriptionResponse();
        }

        public Builder streamer(StreamerResponse streamer) {
            this.subscriptionResponse.streamer = streamer;
            return this;
        }

        public Builder subscriber(IdentityResponse subscriber) {
            this.subscriptionResponse.subscriber = subscriber;
            return this;
        }

        public Builder tariff(Tariff tariff) {
            this.subscriptionResponse.tariff = tariff;
            return this;
        }

        public Builder subscribedAt(String subscribedAt) {
            this.subscriptionResponse.subscribedAt = subscribedAt;
            return this;
        }

        public SubscriptionResponse build() {
            return this.subscriptionResponse;
        }
    }
}
