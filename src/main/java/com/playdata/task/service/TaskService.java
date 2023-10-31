package com.playdata.task.service;

import com.playdata.client.chatgpt.service.ChatGptService;
import com.playdata.domain.articleindex.entity.ArticleIndex;
import com.playdata.domain.articleindex.repository.ArticleIndexRepository;
import com.playdata.domain.task.entity.TaskInformation;
import com.playdata.domain.task.repository.TaskInformationRepository;
import com.playdata.kafka.dto.ArticleKafkaData;
import com.playdata.kafka.producer.StoryProducer;
import lombok.RequiredArgsConstructor;
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

        try {
            List<String> words = chatGptService.parseContent(data.content());

            words.forEach(word->{
                upsertTasks(word, taskIds);
            });

            taskInformationRepository.saveAll(taskInformations);
        } catch (RuntimeException e){
            // TODO : parseContent 예외를 커스텀 예외로 바꾼다면, fit 하게 처리하기
            storyProducer.send(data.id());
        }
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
