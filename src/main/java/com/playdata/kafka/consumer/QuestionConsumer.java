package com.playdata.kafka.consumer;

import com.playdata.kafka.config.TopicConfig;
import com.playdata.kafka.dto.QuestionKafkaData;
import com.playdata.suggest.service.SuggestService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionConsumer {

    private final SuggestService suggestService;

    @KafkaListener(topics = TopicConfig.QUESTION)
    public void listen(QuestionKafkaData data){
        suggestService.taskSuggest(data.id(), data.content());
    }
}
