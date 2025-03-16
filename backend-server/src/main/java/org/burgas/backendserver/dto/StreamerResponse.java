package org.burgas.backendserver.dto;

@SuppressWarnings("unused")
public final class StreamerResponse {

    private Long id;
    private IdentityResponse identity;
    private String firstname;
    private String lastname;
    private String patronymic;
    private String about;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IdentityResponse getIdentity() {
        return identity;
    }

    public void setIdentity(IdentityResponse identity) {
        this.identity = identity;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
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

        public StreamerResponse build() {
            return streamerResponse;
        }
    }
}
