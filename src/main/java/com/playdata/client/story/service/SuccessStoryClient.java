package com.playdata.client.story.service;

import com.playdata.client.story.response.ArticleResponse;

public interface SuccessStoryClient {

    ArticleResponse getById(Long id);
}
