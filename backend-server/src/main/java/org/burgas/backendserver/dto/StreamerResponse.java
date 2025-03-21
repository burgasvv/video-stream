package org.burgas.backendserver.dto;

import org.burgas.backendserver.entity.Category;

import java.util.List;

@SuppressWarnings("unused")
public final class StreamerResponse {

    private Long id;
    private IdentityResponse identity;
    private String firstname;
    private String lastname;
    private String patronymic;
    private String about;
    private Long imageId;
    private List<Category> categories;

    public Long getId() {
        return id;
    }

    public IdentityResponse getIdentity() {
        return identity;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getAbout() {
        return about;
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

        private final StreamerResponse streamerResponse;

        public Builder() {
            streamerResponse = new StreamerResponse();
        }

        public Builder id(Long id) {
            this.streamerResponse.id = id;
            return this;
        }

        public Builder identity(IdentityResponse identity) {
            this.streamerResponse.identity = identity;
            return this;
        }

        public Builder firstname(String firstname) {
            this.streamerResponse.firstname = firstname;
            return this;
        }

        public Builder lastname(String lastname) {
            this.streamerResponse.lastname = lastname;
            return this;
        }

        public Builder patronymic(String patronymic) {
            this.streamerResponse.patronymic = patronymic;
            return this;
        }

        public Builder about(String about) {
            this.streamerResponse.about = about;
            return this;
        }

        public Builder imageId(Long imageId) {
            this.streamerResponse.imageId = imageId;
            return this;
        }

        public Builder categories(List<Category> categories) {
            this.streamerResponse.categories = categories;
            return this;
        }

        public StreamerResponse build() {
            return streamerResponse;
        }
    }
}
