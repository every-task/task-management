package com.playdata.client.chatgpt.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playdata.client.chatgpt.config.GptCompletion;
import com.playdata.client.chatgpt.exception.ChatGptException;
import com.playdata.client.chatgpt.exception.ChatGptExceptionType;
import com.playdata.client.chatgpt.response.GptCompletionResponse;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class ChatGptServiceImpl implements ChatGptService {
    private final OpenAiService openAiService;
    private final ObjectMapper objectMapper;

    @Override
    public List<String> parseContent(String content) {
        CompletableFuture<String> completion = CompletableFuture
                .supplyAsync(() -> completion(content).trim());
        try {
            // TODO : non block, async 로 변경 고민
            return extractWordsFromResponse(completion.get());
        } catch (InterruptedException | ExecutionException e) {
            // TODO : 예외처리 방식 변경
            throw new RuntimeException("CompletableFuture", e);
        }
    }

    public String completion(String prompt) {
        ChatCompletionResult result = openAiService.createChatCompletion(GptCompletion.fromPrompt(prompt));
        GptCompletionResponse response = GptCompletionResponse.of(result);

        return response.getMessages().stream()
                .map(GptCompletionResponse.Message::getMessage)
                .findFirst()
                .orElseThrow(() -> new ChatGptException(ChatGptExceptionType.CHAT_GPT_COMPLETION_FAIL));
    }

    private List<String> extractWordsFromResponse(String response) {
        try {
            return objectMapper.readValue(response, List.class);
        } catch (JsonProcessingException e) {
            throw new ChatGptException(ChatGptExceptionType.COMPLETION_RESPONSE_PARSE_FAIL, e);
        }
    }
}