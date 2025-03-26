package org.burgas.backendserver.config;

import org.burgas.backendserver.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, PasswordEncoder passwordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(urlBasedCorsConfigurationSource()))
                .authenticationManager(authenticationManager())
                .httpBasic(
                        httpBasic -> httpBasic.securityContextRepository(
                                new HttpSessionSecurityContextRepository()
                        )
                )
                .authorizeHttpRequests(
                        requests -> requests
                                .requestMatchers(
                                        "/images/by-id",
                                        "/identities/create",
                                        "/categories", "/categories/by-id", "/categories/async/by-id",
                                        "/categories/sse", "/categories/stream", "/categories/async",
                                        "/videos", "/videos/by-category", "/videos/by-id", "/videos/by-name",
                                        "/videos/stream/by-id", "/videos/stream/by-name",
                                        "/streamers", "/streamers/by-id",
                                        "/streams/by-id"
                                )
                                .permitAll()

                                .requestMatchers(
                                        "/identities/by-id", "/identities/by-id/async", "/identities/update",
                                        "/identities/upload-set-image", "/identities/change-set-image", "/identities/delete-image",
                                        "/streamers/create", "/streamers/update"
                                )
                                .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER", "ROLE_STREAMER")

                                .requestMatchers(
                                        "/videos/upload", "/videos/update", "/videos/delete",
                                        "/streamers/upload-set-image", "/streamers/change-set-image",
                                        "/streamers/delete-image", "/streamers/add-categories",
                                        "/streams/all/by-streamer", "/streams/start", "/streams/update", "/streams/stop",
                                        "/invitations/all/by-receiver", "/invitations/all/by-sender", "/invitations/send",
                                        "/invitations/answer"
                                )
                                .hasAnyAuthority("ROLE_STREAMER")

                                .requestMatchers(
                                        "/identities", "/identities/async",
                                        "/categories/create", "/categories/async/create", "/categories/update",
                                        "/categories/upload-set-image", "/categories/change-set-image",
                                        "/categories/delete-image"
                                )
                                .hasAnyAuthority("ROLE_ADMIN")
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return urlBasedCorsConfigurationSource;
    }
}
