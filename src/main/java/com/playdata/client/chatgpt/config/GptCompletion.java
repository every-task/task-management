package com.playdata.client.chatgpt.config;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;

import java.util.List;

public class GptCompletion {

    public static ChatCompletionRequest fromPrompt(String prompt) {
        return ChatCompletionRequest.builder()
                .messages(convertChatMessage(prompt))
                .maxTokens(MAX_TOKENS)
                .model(MODEL)
                .build();
    }
    private static List<ChatMessage> convertChatMessage(String prompt) {
        return List.of(new ChatMessage(ROLE, REQUEST_SCRIPT + prompt));
    }

    private static final String ROLE = "user";
    private static final int MAX_TOKENS = 1000;
    private static final String MODEL = "gpt-3.5-turbo";
    private static final String REQUEST_SCRIPT = "너는 편집자야\n" +
            "\n" +
            "너가 해야되는 일\n" +
            " \n" +
            "1. 내가 마지막에 요청하는 문장을 핵심만 요약한다.\n" +
            "\n" +
            "2. 요약한 문장의 핵심 요소를 단어로 추출한다.\n" +
            "\n" +
            "3. 추출한 단어 중 글의 핵심과 가장 연관있는 단어를 10개 이하로 추출한다.\n" +
            "\n" +
            "4. 추출한 단어들을 명사로 바꿔서 [\"\", \"\", \"\", \"\", \"\", \"\"] 형태로 대답한다.\n" +
            "\n" +
            "주의사항\n" +
            "1. 잘린 명사는 절대 두개 이상의 단어의 조합이 아니어야 한다.\n" +
            "\n" +
            "2. 사전에 있는 단어만을 명사로 사용한다.\n" +
            "\n" +
            "3. 동사는 절대 포함하지 않는다.\n" +
            "\n" +
            "응답에는 절대 다른 말은 절대 포함하지 않고 리스트로 만든 단어만 포함한다. 아래 문장에 대해서 수행해\n" +
            "\n" +
            "\n";
}