package org.burgas.identityserver.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

public final class Identity implements Persistable<Long> {

    @Id
    private Long id;
    private String nickname;
    private String password;
    private String email;
    private Boolean enabled;
    private Long authorityId;

    @Transient
    private Boolean isNew;

    @Override
    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(Long id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public String getNickname() {
        return nickname;
    }

    @SuppressWarnings("unused")
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @SuppressWarnings("unused")
    public String getPassword() {
        return password;
    }

    @SuppressWarnings("unused")
    public void setPassword(String password) {
        this.password = password;
    }

    @SuppressWarnings("unused")
    public String getEmail() {
        return email;
    }

    @SuppressWarnings("unused")
    public void setEmail(String email) {
        this.email = email;
    }

    @SuppressWarnings("unused")
    public Boolean getEnabled() {
        return enabled;
    }

    @SuppressWarnings("unused")
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @SuppressWarnings("unused")
    public Long getAuthorityId() {
        return authorityId;
    }

    @SuppressWarnings("unused")
    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }

    @Override
    public boolean isNew() {
        return isNew || id == null;
    }

    @SuppressWarnings("unused")
    public Boolean getNew() {
        return isNew || id == null;
    }

    @SuppressWarnings("unused")
    public void setNew(Boolean aNew) {
        isNew = aNew;
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

        public Builder isNew(Boolean isNew) {
            this.identity.isNew = isNew;
            return this;
        }

        public Identity build() {
            return identity;
        }
    }
}
