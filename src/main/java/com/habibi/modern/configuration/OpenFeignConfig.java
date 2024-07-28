package com.habibi.modern.configuration;

import com.habibi.modern.exceptions.ModernErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenFeignConfig {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new ModernErrorDecoder();
    }
}