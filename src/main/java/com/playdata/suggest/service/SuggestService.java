package com.playdata.suggest.service;

import com.playdata.articleindex.service.ArticleIndexService;
import com.playdata.client.chatgpt.service.ChatGptService;
import com.playdata.domain.suggest.entity.Suggest;
import com.playdata.domain.suggest.repository.SuggestRepository;
import com.playdata.domain.task.entity.TaskInformation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SuggestService {

    private final ChatGptService chatGptService;
    private final SuggestRepository suggestRepository;
    private final ArticleIndexService articleIndexService;

    private final int SUGGEST_TASK_COUNT = 5;

    public void taskSuggest(Long questionId, String content){
        List<String> words = chatGptService.parseContent(content);

        List<UUID> relatedTaskIds = articleIndexService.getRelatedTaskIds(words, SUGGEST_TASK_COUNT);

        List<Suggest> suggests = relatedTaskIds.stream()
                .map(taskId->Suggest.createSuggest(questionId, TaskInformation.fromId(taskId)))
                .toList();

        suggestRepository.saveAll(suggests);
    }
}
