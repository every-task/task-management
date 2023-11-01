package com.playdata.kafka.producer;

import com.playdata.kafka.config.TopicConfig;
import com.playdata.kafka.dto.QuestionFailKafkaData;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionProducer {

    private final KafkaTemplate<String, QuestionFailKafkaData> kafkaTemplate;

    public void send(Long questionId){
        kafkaTemplate.send(TopicConfig.QUESTION_INDEX_FAIL, QuestionFailKafkaData.fromId(questionId));
    }
}
