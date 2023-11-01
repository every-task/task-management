package com.playdata.kafka.dto;

public record QuestionFailKafkaData(Long id) {

    public static QuestionFailKafkaData fromId(Long id){
        return new QuestionFailKafkaData(id);
    }
}
