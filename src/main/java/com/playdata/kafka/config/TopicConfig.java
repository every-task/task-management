package com.playdata.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfig {

    public static final String SUCCESS_STORY = "successStory";

    @Bean
    public NewTopic topicSuccessStory(){
        return TopicBuilder
                .name(SUCCESS_STORY)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
