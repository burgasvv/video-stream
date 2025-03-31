package org.burgas.backendserver.dto;

@SuppressWarnings("unused")
public final class FollowUpResponse {

    private FollowedStreamer streamer;
    private Follower follower;
    private String followedAt;

    public FollowedStreamer getStreamer() {
        return streamer;
    }

    public Follower getFollower() {
        return follower;
    }

    public String getFollowedAt() {
        return followedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final FollowUpResponse followUpResponse;

        public Builder() {
            followUpResponse = new FollowUpResponse();
        }

        public Builder streamer(FollowedStreamer streamer) {
            this.followUpResponse.streamer = streamer;
            return this;
        }

        public Builder follower(Follower follower) {
            this.followUpResponse.follower = follower;
            return this;
        }

        public Builder followedAt(String followedAt) {
            this.followUpResponse.followedAt = followedAt;
            return this;
        }

        public FollowUpResponse build() {
            return followUpResponse;
        }
    }
}
