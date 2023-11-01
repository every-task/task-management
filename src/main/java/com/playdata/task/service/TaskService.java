package com.playdata.task.service;

import com.playdata.client.chatgpt.service.ChatGptService;
import com.playdata.domain.articleindex.entity.ArticleIndex;
import com.playdata.domain.articleindex.repository.ArticleIndexRepository;
import com.playdata.domain.task.entity.TaskInformation;
import com.playdata.domain.task.repository.TaskInformationRepository;
import com.playdata.kafka.dto.ArticleKafkaData;
import com.playdata.kafka.producer.StoryProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskInformationRepository taskInformationRepository;
    private final ArticleIndexRepository articleIndexRepository;
    private final ChatGptService chatGptService;
    private final StoryProducer storyProducer;

    @Async
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000L))
    public void taskRegister(ArticleKafkaData data){

        List<TaskInformation> taskInformations = data.tasks().stream()
                .map(task -> TaskInformation.createTask(
                        task.id(),
                        task.content(),
                        task.period()))
                .toList();

        Set<UUID> taskIds = taskInformations.stream()
                .map(TaskInformation::getId)
                .collect(Collectors.toSet());

        List<String> words = chatGptService.parseContent(data.content());

        words.forEach(word->{
            upsertTasks(word, taskIds);
        });

        taskInformationRepository.saveAll(taskInformations);
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
