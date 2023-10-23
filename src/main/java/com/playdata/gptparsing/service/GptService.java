package com.playdata.gptparsing.service;


import com.playdata.gptparsing.dto.request.GptCompletionRequest;
import com.playdata.gptparsing.dto.response.GptCompletionResponse;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GptService {
    private final OpenAiService openAiService;


    public GptCompletionResponse completion(final GptCompletionRequest restRequest) {
        CompletionResult result = openAiService.createCompletion(GptCompletionRequest.of(restRequest));
        GptCompletionResponse response = GptCompletionResponse.of(result);

        List<String> messages = response.getMessages().stream()
                .map(GptCompletionResponse.Message::getText)
                .collect(Collectors.toList());


        return response;
    }

}