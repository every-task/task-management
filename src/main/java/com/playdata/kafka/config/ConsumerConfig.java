package com.playdata.kafka.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;

@Configuration
@RequiredArgsConstructor
public class ConsumerConfig {

    @Bean
    public RecordMessageConverter converter() {
        return new JsonMessageConverter();
    }
}
