package com.playdata.kafka.dto;

import java.util.List;
import java.util.UUID;

public record SuggestKafkaData(Long questionId, List<UUID> taskIds) {

    public static SuggestKafkaData of(Long questionId, List<UUID> taskIds){
        return new SuggestKafkaData(questionId, taskIds);
    }
}
