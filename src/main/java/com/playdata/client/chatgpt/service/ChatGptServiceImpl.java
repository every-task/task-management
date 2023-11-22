package com.playdata.client.chatgpt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playdata.client.chatgpt.config.GptCompletion;
import com.playdata.client.chatgpt.exception.ChatGptException;
import com.playdata.client.chatgpt.exception.ChatGptExceptionType;
import com.playdata.client.chatgpt.response.GptCompletionResponse;
import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatGptServiceImpl implements ChatGptService {
    private final OpenAiService openAiService;
    private final ObjectMapper objectMapper;

    @Override
    public CompletableFuture<List<String>> parseContent(String content) {
        return CompletableFuture.supplyAsync(() -> completion(content))
                .thenApply(this::extractWords);
    }

    public String completion(String prompt) {
        try {
            ChatCompletionResult result = openAiService.createChatCompletion(GptCompletion.fromPrompt(prompt));
            return extractMessage(result);
        } catch (OpenAiHttpException e){
            throw new ChatGptException(ChatGptExceptionType.CHAT_GPT_COMPLETION_FAIL, e);
        }
    }

    private String extractMessage(ChatCompletionResult result) {
        GptCompletionResponse response = GptCompletionResponse.of(result);

        return response.getMessages().stream()
                .map(GptCompletionResponse.Message::getMessage)
                .findFirst()
                .orElseThrow(() -> new ChatGptException(ChatGptExceptionType.NO_RESPONSE));
    }

    private List<String> extractWords(String message){
        try {
            return objectMapper.readValue(message.trim(), List.class);
        } catch (JsonProcessingException e) {
            throw new ChatGptException(ChatGptExceptionType.COMPLETION_RESPONSE_PARSE_FAIL, e);
        }
    }
}