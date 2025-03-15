package org.burgas.identityserver.config;

import org.burgas.identityserver.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
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
                .csrf(
                        csrf -> csrf
                                .csrfTokenRequestHandler(new XorCsrfTokenRequestAttributeHandler())
                )
                .cors(cors -> cors.configurationSource(urlBasedCorsConfigurationSource()))
                .authenticationManager(authenticationManager())
                .httpBasic(
                        basicConfigurer -> basicConfigurer
                                .securityContextRepository(new HttpSessionSecurityContextRepository())
                )
                .authorizeHttpRequests(
                        requests -> requests
                                .requestMatchers(
                                        "/authentication/principal", "/authentication/token",
                                        "/identities/create",
                                        "/videos", "/videos/by-id", "/videos/by-name",
                                        "/videos/stream/by-id", "/videos/stream/by-name"
                                )
                                .permitAll()

                                .requestMatchers(
                                        "/identities/by-id", "/identities/update",
                                        "/videos/upload", "/videos/update", "/videos/delete"
                                )
                                .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER", "ROLE_STREAMER")

                                .requestMatchers(
                                        "/identities"
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
