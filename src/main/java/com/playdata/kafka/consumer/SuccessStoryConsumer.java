package com.playdata.kafka.consumer;

import com.playdata.kafka.config.TopicConfig;
import com.playdata.kafka.dto.ArticleKafkaData;
import com.playdata.task.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SuccessStoryConsumer {

    private final TaskService taskService;

    @KafkaListener(topics = TopicConfig.SUCCESS_STORY)
    public void listen(ArticleKafkaData data){
        taskService.taskRegister(data);
    }
}
