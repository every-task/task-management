package com.playdata.client.chatgpt.service;


import com.playdata.client.chatgpt.request.GptCompletionRequest;
import com.playdata.client.chatgpt.response.GptCompletionResponse;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatGptServiceImpl implements ChatGptService {
    private final OpenAiService openAiService;

    @Override
    public List<String> parseContent(String content) {
        String completion = completion(content);
        List<String> word = extractWordsFromResponse(completion);
        // 쉼표로 구분해서 리스트로 만들어서 반환
        return word;

    }

    public String completion(String prompt) {
        CompletionResult result = openAiService.createCompletion(GptCompletionRequest.fromPrompt(prompt));
        GptCompletionResponse response = GptCompletionResponse.of(result);

        return response.getMessages().stream()
                .map(GptCompletionResponse.Message::getText)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private List<String> extractWordsFromResponse(String response) {
        String[] wordsArray = response.replaceAll("[\"\\[\\]]", "").split(", ");
        return Arrays.asList(wordsArray);
    }
}