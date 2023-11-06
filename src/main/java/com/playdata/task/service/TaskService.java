package com.playdata.task.service;

import com.playdata.client.chatgpt.service.ChatGptService;
import com.playdata.domain.articleindex.entity.ArticleIndex;
import com.playdata.domain.articleindex.repository.ArticleIndexRepository;
import com.playdata.kafka.dto.ArticleKafkaData;
import com.playdata.kafka.dto.TaskKafkaData;
import com.playdata.kafka.producer.StoryProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final ArticleIndexRepository articleIndexRepository;
    private final ChatGptService chatGptService;
    private final StoryProducer storyProducer;

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000L))
    public void taskRegister(ArticleKafkaData data){

        Set<UUID> taskIds = data.tasks().stream()
                .map(TaskKafkaData::id)
                .collect(Collectors.toSet());

        List<String> words = chatGptService.parseContent(data.content());

        words.forEach(word->{
            upsertTasks(word, taskIds);
        });
    }

    @Recover
    public void recover(RuntimeException e, ArticleKafkaData data){
        storyProducer.send(data.id());
    }

    public void upsertTasks(String word, Set<UUID> taskIds) {
        Optional<ArticleIndex> articleIndex = articleIndexRepository.findById(word);

        if(articleIndex.isEmpty()){
            articleIndexRepository.save(ArticleIndex.createArticleIndex(word, taskIds));
        } else {
            ArticleIndex findArticleIndex = articleIndex.get();
            findArticleIndex.getTasks().addAll(taskIds);
            articleIndexRepository.save(findArticleIndex);
        }
    }
}
