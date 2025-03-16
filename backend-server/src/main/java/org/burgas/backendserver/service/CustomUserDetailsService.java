package org.burgas.backendserver.service;

import org.burgas.backendserver.dto.IdentityResponse;
import org.burgas.backendserver.mapper.IdentityMapper;
import org.burgas.backendserver.repository.IdentityRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final IdentityRepository identityRepository;
    private final IdentityMapper identityMapper;

    public CustomUserDetailsService(IdentityRepository identityRepository, IdentityMapper identityMapper) {
        this.identityRepository = identityRepository;
        this.identityMapper = identityMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return identityRepository
                .findIdentityByEmail(username)
                .map(identityMapper::toIdentityResponse)
                .orElseGet(IdentityResponse::new);
    }
}
