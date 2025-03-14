package org.burgas.identityserver.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public final class IdentityResponse implements UserDetails {

    private Long id;
    private String nickname;
    private String password;
    private String email;
    private Boolean enabled;
    private AuthorityResponse authority;

    @SuppressWarnings("unused")
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority(authority.getName())
        );
    }

    @SuppressWarnings("unused")
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return enabled || UserDetails.super.isEnabled();
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
    public AuthorityResponse getAuthority() {
        return authority;
    }

    @SuppressWarnings("unused")
    public void setAuthority(AuthorityResponse authority) {
        this.authority = authority;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final IdentityResponse identityResponse;

        public Builder() {
            identityResponse = new IdentityResponse();
        }

        public Builder id(Long id) {
            this.identityResponse.id = id;
            return this;
        }

        public Builder nickname(String nickname) {
            this.identityResponse.nickname = nickname;
            return this;
        }

        public Builder password(String password) {
            this.identityResponse.password = password;
            return this;
        }

        public Builder email(String email) {
            this.identityResponse.email = email;
            return this;
        }

        public Builder enabled(Boolean enabled) {
            this.identityResponse.enabled = enabled;
            return this;
        }

        public Builder authority(AuthorityResponse authority) {
            this.identityResponse.authority = authority;
            return this;
        }

        public IdentityResponse build() {
            return identityResponse;
        }
    }
}
