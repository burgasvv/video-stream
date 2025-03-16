package org.burgas.streamservice.dto;

@SuppressWarnings("unused")
public final class IdentityPrincipal {

    private Long id;
    private String nickname;
    private String password;
    private String email;
    private Boolean enabled;
    private String authority;
    private Boolean authenticated;

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public String getAuthority() {
        return authority;
    }

    public Boolean getAuthenticated() {
        return authenticated;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        public final IdentityPrincipal identityPrincipal;

        public Builder() {
            identityPrincipal = new IdentityPrincipal();
        }

        public Builder id(Long id) {
            this.identityPrincipal.id = id;
            return this;
        }

        public Builder nickname(String nickname) {
            this.identityPrincipal.nickname = nickname;
            return this;
        }

        public Builder password(String password) {
            this.identityPrincipal.password = password;
            return this;
        }

        public Builder email(String email) {
            this.identityPrincipal.email = email;
            return this;
        }

        public Builder enabled(Boolean enabled) {
            this.identityPrincipal.enabled = enabled;
            return this;
        }

        public Builder authority(String authority) {
            this.identityPrincipal.authority = authority;
            return this;
        }

        public Builder authenticated(Boolean authenticated) {
            this.identityPrincipal.authenticated = authenticated;
            return this;
        }

        public IdentityPrincipal build() {
            return identityPrincipal;
        }
    }
}
