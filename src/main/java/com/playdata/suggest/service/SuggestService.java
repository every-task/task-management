package com.playdata.suggest.service;

import com.playdata.articleindex.service.ArticleIndexService;
import com.playdata.client.chatgpt.exception.ChatGptException;
import com.playdata.client.chatgpt.service.ChatGptService;
import com.playdata.kafka.dto.QuestionKafkaData;
import com.playdata.kafka.producer.QuestionProducer;
import com.playdata.kafka.producer.SuggestProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class SuggestService {

    private final ChatGptService chatGptService;
    private final ArticleIndexService articleIndexService;
    private final QuestionProducer questionProducer;
    private final SuggestProducer suggestProducer;

    private final static int SUGGEST_TASK_COUNT = 5;

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000L))
    public void taskSuggest(QuestionKafkaData data){
        CompletableFuture<List<String>> parsedContent = chatGptService.parseContent(data.content());
        parsedContent
                .thenApply(words -> articleIndexService.getRelatedTaskIds(words, data.category(), SUGGEST_TASK_COUNT))
                .thenAccept(relatedTaskIds -> suggestProducer.send(data.id(), relatedTaskIds));
    }

    @Recover
    public void recover(ChatGptException e, Long questionId, String content){
        log.error("ChatGptException : {} Question ID : {}", e, questionId);
        questionProducer.send(questionId);
    }
}
