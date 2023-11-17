package com.playdata.kafka.consumer;

import com.playdata.kafka.config.KafkaData;
import com.playdata.kafka.config.TopicConfig;
import com.playdata.kafka.dto.ArticleKafkaData;
import com.playdata.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoryConsumer {

    private final TaskService taskService;

    @KafkaListener(topics = TopicConfig.STORY)
    public void listen(KafkaData<ArticleKafkaData> data){
        ArticleKafkaData articleKafkaData = data.data();
        log.info("Received Story Id : {} Topic : {}", articleKafkaData.id(), TopicConfig.STORY);
        taskService.taskRegister(articleKafkaData);
    }
}
