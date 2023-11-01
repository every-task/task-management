package com.playdata.kafka.consumer;

import com.playdata.kafka.config.TopicConfig;
import com.playdata.kafka.dto.ArticleKafkaData;
import com.playdata.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoryConsumer {

    private final TaskService taskService;

    @KafkaListener(topics = TopicConfig.STORY)
    public void listen(ArticleKafkaData data){
        taskService.taskRegister(data);
    }
}
