package com.playdata.client.chatgpt;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatGptServiceImpl implements ChatCptService{
    @Override
    public List<String> parseContent(String content) {
        return List.of("수능", "의대", "수학");
    }
}
