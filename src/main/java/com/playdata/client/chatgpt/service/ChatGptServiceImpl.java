package com.playdata.client.chatgpt.service;


import com.playdata.client.chatgpt.request.GptCompletionRequest;
import com.playdata.client.chatgpt.response.GptCompletionResponse;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatGptServiceImpl implements ChatGptService {
    private final OpenAiService openAiService;

    @Override
    public List<String> parseContent(String content) {
        String completion = completion(content);
        // 쉼표로 구분해서 리스트로 만들어서 반환
        return List.of(completion);
    }

    public String completion(String prompt) {
        CompletionResult result = openAiService.createCompletion(GptCompletionRequest.fromPrompt(prompt));
        GptCompletionResponse response = GptCompletionResponse.of(result);

        return response.getMessages().stream()
                .map(GptCompletionResponse.Message::getText)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}