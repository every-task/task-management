package com.playdata.client.story.service;

import com.playdata.client.story.response.ArticleResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class SuccessStoryClientImpl implements SuccessStoryClient{

    private final String API_URL = "http://localhost:8080/api/v1/SuccessStory";
    @Override
    public ArticleResponse getById(Long id) {
        return WebClient.create()
                .get()
                .uri(API_URL + "/" + id)
                .retrieve()
                .bodyToMono(ArticleResponse.class)
                .block();
    }
}
