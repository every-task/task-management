package com.playdata.suggest;

import com.playdata.client.chatgpt.service.ChatGptService;
import com.playdata.client.question.response.QuestionResponse;
import com.playdata.client.question.service.QuestionClient;
import com.playdata.domain.articleindex.entity.ArticleIndex;
import com.playdata.domain.articleindex.repository.ArticleIndexRepository;
import com.playdata.domain.suggest.entity.Suggest;
import com.playdata.domain.suggest.repository.SuggestRepository;
import com.playdata.domain.task.entity.TaskInformation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SuggestService {

    private final ChatGptService chatGptService;
    private final QuestionClient questionClient;
    private final ArticleIndexRepository articleIndexRepository;
    private final SuggestRepository suggestRepository;

    private final int SUGGEST_TASK_COUNT = 5;

    public void taskSuggest(Long questionId){
        QuestionResponse questionResponse = questionClient.getById(questionId);

        List<String> words = chatGptService.parseContent(questionResponse.content());

        List<ArticleIndex> articleIndices = articleIndexRepository.findByWordIn(words);

        List<Suggest> suggests = getRandomSuggest(questionId, articleIndices);

        suggestRepository.saveAll(suggests);
    }

    private List<Suggest> getRandomSuggest(Long questionId, List<ArticleIndex> articleIndices) {
        ArrayList<UUID> taskIds = articleIndices.stream()
                .map(ArticleIndex::getTasks)
                .flatMap(Set::stream)
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));

        Collections.shuffle(taskIds);

        return taskIds.subList(0, SUGGEST_TASK_COUNT).stream()
                .map(taskId -> Suggest.createSuggest(
                        questionId,
                        TaskInformation.fromId(taskId)))
                .toList();
    }
}
