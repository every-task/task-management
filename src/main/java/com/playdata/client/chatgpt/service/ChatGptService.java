package com.playdata.client.chatgpt.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ChatGptService {

    CompletableFuture<List<String>> parseContent(String content);
}
