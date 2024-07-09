package com.habibi.modern.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    @Value("${resttemplate.client.connect.timeout}")
    private Long restTemplateClientConnectTimeout;

    @Value("${resttemplate.client.read.timeout}")
    private Long restTemplateClientReadTimeout;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.setConnectTimeout(Duration.ofSeconds(restTemplateClientConnectTimeout))
                .setReadTimeout(Duration.ofSeconds(restTemplateClientReadTimeout)).build();
    }
}