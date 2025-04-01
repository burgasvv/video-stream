package org.burgas.backendserver.dto;

@SuppressWarnings("unused")
public final class FollowResponse {

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

        private final FollowResponse followResponse;

        public Builder() {
            followResponse = new FollowResponse();
        }

        public Builder streamer(FollowedStreamer streamer) {
            this.followResponse.streamer = streamer;
            return this;
        }

        public Builder follower(Follower follower) {
            this.followResponse.follower = follower;
            return this;
        }

        public Builder followedAt(String followedAt) {
            this.followResponse.followedAt = followedAt;
            return this;
        }

        public FollowResponse build() {
            return followResponse;
        }
    }
}
