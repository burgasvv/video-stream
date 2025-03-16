package org.burgas.identityserver.dto;

public final class IdentityPrincipal {

    private Long id;
    private String nickname;
    private String password;
    private String email;
    private Boolean enabled;
    private String authority;
    private Boolean authenticated;

    @SuppressWarnings("unused")
    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public String getNickname() {
        return nickname;
    }

    @SuppressWarnings("unused")
    public String getPassword() {
        return password;
    }

    @SuppressWarnings("unused")
    public String getEmail() {
        return email;
    }

    @SuppressWarnings("unused")
    public Boolean getEnabled() {
        return enabled;
    }

    @SuppressWarnings("unused")
    public String getAuthority() {
        return authority;
    }

    @SuppressWarnings("unused")
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
