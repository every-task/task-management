package com.playdata.client.chatgpt.request;

import com.theokanning.openai.completion.CompletionRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GptCompletionRequest {

    private String prompt;
    private String model;
    private Integer maxToken;

    public static CompletionRequest fromPrompt(String prompt){
        return CompletionRequest.builder()
                .prompt(prompt)
                .model("text-davinci-003")
                .prompt(prompt)
                .maxTokens(1000)
                .build();
    }
}