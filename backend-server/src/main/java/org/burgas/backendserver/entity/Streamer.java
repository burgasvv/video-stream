package org.burgas.backendserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuppressWarnings("unused")
public final class Streamer {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long identityId;
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

    public Long getIdentityId() {
        return identityId;
    }

    public void setIdentityId(Long identityId) {
        this.identityId = identityId;
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

        private final Streamer streamer;

        public Builder() {
            streamer = new Streamer();
        }

        public Builder id(Long id) {
            this.streamer.id = id;
            return this;
        }

        public Builder identityId(Long identityId) {
            this.streamer.identityId = identityId;
            return this;
        }

        public Builder firstname(String firstname) {
            this.streamer.firstname = firstname;
            return this;
        }

        public Builder lastname(String lastname) {
            this.streamer.lastname = lastname;
            return this;
        }

        public Builder patronymic(String patronymic) {
            this.streamer.patronymic = patronymic;
            return this;
        }

        public Builder about(String about) {
            this.streamer.about = about;
            return this;
        }

        public Streamer build() {
            return streamer;
        }
    }
}
