package com.playdata.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfig {

    public static final String STORY = "story";
    public static final String STORY_INDEX_FAIL = "story-index-fail";
    public static final String QUESTION = "question";
    public static final String QUESTION_INDEX_FAIL = "question-index-fail";

    // local 개발용 토픽, 운영환경은 task service 에서 토픽 생성 X
    @Bean
    public NewTopic topicStory(){
        return TopicBuilder
                .name(STORY)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topicStoryIndexFail(){
        return TopicBuilder
                .name(STORY_INDEX_FAIL)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topicQuestion(){
        return TopicBuilder
                .name(QUESTION)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topicQuestionIndexFail(){
        return TopicBuilder
                .name(QUESTION_INDEX_FAIL)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
