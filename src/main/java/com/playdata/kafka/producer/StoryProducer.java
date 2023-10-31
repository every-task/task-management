package com.playdata.kafka.producer;

import com.playdata.kafka.config.TopicConfig;
import com.playdata.kafka.dto.StoryFailKafkaData;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoryProducer {

    private final KafkaTemplate<String, StoryFailKafkaData> kafkaTemplate;

    public void send(Long storyId){
        kafkaTemplate.send(TopicConfig.STORY_INDEX_FAIL, StoryFailKafkaData.fromId(storyId));
    }
}
