package com.playdata.kafka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;

@Configuration
public class ConsumerConfig {

    @Bean
    public RecordMessageConverter recordMessageConverter() {
        return new JsonMessageConverter();
    }
}
