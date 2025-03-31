package org.burgas.backendserver.dto;

@SuppressWarnings("unused")
public final class Follower {

    private Long id;
    private String nickname;
    private Long imageId;

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public Long getImageId() {
        return imageId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Follower follower;

        public Builder() {
            follower = new Follower();
        }

        public Builder id(Long id) {
            this.follower.id = id;
            return this;
        }

        public Builder nickname(String nickname) {
            this.follower.nickname = nickname;
            return this;
        }

        public Builder imageId(Long imageId) {
            this.follower.imageId = imageId;
            return this;
        }

        public Follower build() {
            return follower;
        }
    }
}
