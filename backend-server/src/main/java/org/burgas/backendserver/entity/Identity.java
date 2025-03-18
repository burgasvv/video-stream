package org.burgas.backendserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuppressWarnings("unused")
public final class Identity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String nickname;
    private String password;
    private String email;
    private Boolean enabled;
    private Long authorityId;
    private Long imageId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Identity identity;

        public Builder() {
            identity = new Identity();
        }

        public Builder id(Long id) {
            this.identity.id = id;
            return this;
        }

        public Builder nickname(String nickname) {
            this.identity.nickname = nickname;
            return this;
        }

        public Builder password(String password) {
            this.identity.password = password;
            return this;
        }

        public Builder email(String email) {
            this.identity.email = email;
            return this;
        }

        public Builder enabled(Boolean enabled) {
            this.identity.enabled = enabled;
            return this;
        }

        public Builder authorityId(Long authorityId) {
            this.identity.authorityId = authorityId;
            return this;
        }

        public Builder imageId(Long imageId) {
            this.identity.imageId = imageId;
            return this;
        }

        public Identity build() {
            return identity;
        }
    }
}
