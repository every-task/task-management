package com.playdata.client.question.service;

import com.playdata.client.question.response.QuestionResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class QuestionClientImpl implements QuestionClient{
    private final String API_URL = "http://localhost:8080/api/v1/SuccessStory";
    @Override
    public QuestionResponse getById(Long id) {

        return WebClient.create()
                .get()
                .uri(API_URL + "/" + id)
                .retrieve()
                .bodyToMono(QuestionResponse.class)
                .block();
    }
}
