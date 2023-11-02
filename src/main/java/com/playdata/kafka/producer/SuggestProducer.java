package com.playdata.kafka.producer;

import com.playdata.kafka.config.TopicConfig;
import com.playdata.kafka.dto.SuggestKafkaData;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SuggestProducer {

    private final KafkaTemplate<String, SuggestKafkaData> kafkaTemplate;

    public void send(Long questionId, List<UUID> taskIds){
        kafkaTemplate.send(TopicConfig.SUGGEST, SuggestKafkaData.of(questionId, taskIds));
    }
}
