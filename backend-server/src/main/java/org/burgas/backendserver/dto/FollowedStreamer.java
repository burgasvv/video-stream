package org.burgas.backendserver.dto;

import org.burgas.backendserver.entity.Category;

import java.util.List;

@SuppressWarnings("unused")
public final class FollowedStreamer {

    private Long id;
    private String nickname;
    private Long imageId;
    private List<Category> categories;

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public Long getImageId() {
        return imageId;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final FollowedStreamer followedStreamer;

        public Builder() {
            followedStreamer = new FollowedStreamer();
        }

        public Builder id(Long id) {
            this.followedStreamer.id = id;
            return this;
        }

        public Builder nickname(String nickname) {
            this.followedStreamer.nickname = nickname;
            return this;
        }

        public Builder imageId(Long imageId) {
            this.followedStreamer.imageId = imageId;
            return this;
        }

        public Builder categories(List<Category> categories) {
            this.followedStreamer.categories = categories;
            return this;
        }

        public FollowedStreamer build() {
            return followedStreamer;
        }
    }
}
