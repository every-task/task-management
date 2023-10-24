package com.playdata.client.question.service;

import com.playdata.client.question.response.QuestionResponse;

public interface QuestionClient {
    QuestionResponse getById(Long id);
}
