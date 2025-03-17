package org.burgas.backendserver;

import org.burgas.backendserver.filter.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestClient;

@SpringBootApplication
@ServletComponentScan(
        basePackageClasses = {
                GetIdentityByIdFilter.class,
                GetIdentitiesFilter.class,
                UpdateIdentityFilter.class,
                UploadVideoFilter.class,
                UpdateDeleteVideoFilter.class
        }
)
public class BackendServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendServerApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestClient restClient() {
        return RestClient.builder().build();
    }
}
