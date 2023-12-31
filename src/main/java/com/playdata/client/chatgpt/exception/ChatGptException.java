package com.playdata.client.chatgpt.exception;

public class ChatGptException extends RuntimeException {

    public ChatGptException(Throwable cause){
        super(cause);
    }

    public ChatGptException(ChatGptExceptionType type){
        super(type.name());
    }

    public ChatGptException(ChatGptExceptionType type, Throwable cause){
        super(type.name(), cause);
    }
}
