package com.makschornyi.monitorwindows.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class Configurations {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
