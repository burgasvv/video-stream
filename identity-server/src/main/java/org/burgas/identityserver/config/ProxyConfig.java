package org.burgas.identityserver.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProxyConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder
                .routes()
                .route(
                        "video-service",
                        predicateSpec -> predicateSpec
                                        .path("/videos/**")
                                        .uri("http://localhost:9000")
                )
                .build();
    }
}
