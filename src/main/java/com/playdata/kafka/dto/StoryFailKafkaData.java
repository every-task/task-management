package com.playdata.kafka.dto;

public record StoryFailKafkaData(Long id) {

    public static StoryFailKafkaData fromId(Long id){
        return new StoryFailKafkaData(id);
    }
}
