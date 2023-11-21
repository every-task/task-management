package com.playdata.suggest.service;

import com.playdata.articleindex.service.ArticleIndexService;
import com.playdata.client.chatgpt.exception.ChatGptException;
import com.playdata.client.chatgpt.service.ChatGptService;
import com.playdata.kafka.dto.QuestionKafkaData;
import com.playdata.kafka.producer.QuestionProducer;
import com.playdata.kafka.producer.SuggestProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public void taskSuggest(QuestionKafkaData data){
        CompletableFuture<List<String>> parsedContent = chatGptService.parseContent(data.content());
        parsedContent
                .thenApply(words -> articleIndexService.getRelatedTaskIds(words, data.category(), SUGGEST_TASK_COUNT))
                .thenAccept(relatedTaskIds -> suggestProducer.send(data.id(), relatedTaskIds))
                .exceptionally(e -> {
                    recover(new ChatGptException(e), data);
                    return null;
                });
    }

    public void recover(ChatGptException e, QuestionKafkaData data){
        log.error("fail indexing question Question Id : {}", data.id(), e);
        questionProducer.send(data.id());
    }
}
