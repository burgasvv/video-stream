package org.burgas.backendserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.MessageChannel;

@Configuration
@EnableIntegration
public class IntegrationConfig {

    @Bean(value = "message-send-channel")
    public MessageChannel requestChannel() {
        return new DirectChannel();
    }
}
