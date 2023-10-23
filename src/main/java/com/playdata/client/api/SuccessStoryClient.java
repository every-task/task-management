package com.playdata.client.api;

import com.playdata.client.response.ArticleResponse;

public interface SuccessStoryClient {

    ArticleResponse getById(Long id);
}
