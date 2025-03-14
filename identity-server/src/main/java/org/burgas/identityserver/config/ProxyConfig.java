package org.burgas.identityserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@Configuration
public class ProxyConfig {

    private static final String VIDEO_SERVICE_URL = "http://localhost:9000";

    @Bean
    public RouterFunction<ServerResponse> router() {
        return RouterFunctions
                .route()

                .GET("/videos/**", http(VIDEO_SERVICE_URL))
                .POST("/videos/**", http(VIDEO_SERVICE_URL))
                .PUT("/videos/**", http(VIDEO_SERVICE_URL))
                .DELETE("/videos/**", http(VIDEO_SERVICE_URL))

                .build();
    }
}
