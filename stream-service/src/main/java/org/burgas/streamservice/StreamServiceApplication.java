package org.burgas.streamservice;

import org.burgas.streamservice.filter.CreateStreamerFilter;
import org.burgas.streamservice.filter.UpdateStreamerFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@SpringBootApplication
@ServletComponentScan(
        basePackageClasses = {
                CreateStreamerFilter.class,
                UpdateStreamerFilter.class
        }
)
public class StreamServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamServiceApplication.class, args);
    }

    @Bean
    public RestClient restClient() {
        return RestClient.builder().build();
    }

}
