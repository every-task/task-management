package com.playdata.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfig {

    public static final String STORY = "story";
    public static final String QUESTION = "question";

//    @Bean
//    public NewTopic topicSuccessStory(){
//        return TopicBuilder
//                .name(STORY)
//                .partitions(1)
//                .replicas(1)
//                .build();
//    }
//    @Bean
//    public NewTopic topicQuestion(){
//        return TopicBuilder
//                .name(QUESTION)
//                .partitions(1)
//                .replicas(1)
//                .build();
//    }
}
