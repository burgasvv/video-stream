package org.burgas.backendserver.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public final class IdentityResponse implements UserDetails {

    private Long id;
    private String nickname;
    private String password;
    private String email;
    private Boolean enabled;
    private AuthorityResponse authority;
    private Long imageId;

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority(authority.getName())
        );
    }

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

    public String getEmail() {
        return email;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public AuthorityResponse getAuthority() {
        return authority;
    }

    public Long getImageId() {
        return imageId;
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

        public Builder imageId(Long imageId) {
            this.identityResponse.imageId = imageId;
            return this;
        }

        public IdentityResponse build() {
            return identityResponse;
        }
    }
}
