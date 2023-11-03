package com.playdata.suggest.service;

import com.playdata.articleindex.service.ArticleIndexService;
import com.playdata.client.chatgpt.service.ChatGptService;
import com.playdata.kafka.producer.QuestionProducer;
import com.playdata.kafka.producer.SuggestProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SuggestService {

    private final ChatGptService chatGptService;
    private final ArticleIndexService articleIndexService;
    private final QuestionProducer questionProducer;
    private final SuggestProducer suggestProducer;

    private  final static int SUGGEST_TASK_COUNT = 5;

    @Async
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000L))
    public void taskSuggest(Long questionId, String content){
        List<String> words = chatGptService.parseContent(content);

        List<UUID> relatedTaskIds = articleIndexService.getRelatedTaskIds(words, SUGGEST_TASK_COUNT);

        suggestProducer.send(questionId, relatedTaskIds);
    }

    @Recover
    public void recover(RuntimeException e, Long questionId, String content){
        questionProducer.send(questionId);
    }

}
