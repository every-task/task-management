package com.playdata.kafka.config;

import lombok.Builder;

@Builder
public record KafkaData<T>(
        T data,
        KafkaAction action
) {
    public static <T>KafkaData<T> create(T data){
        return KafkaData.<T>builder()
                .action(KafkaAction.CREATE)
                .data(data)
                .build();
    }

    public static <T>KafkaData<T> update(T data){
        return KafkaData.<T>builder()
                .action(KafkaAction.UPDATE)
                .data(data)
                .build();
    }

    public static <T>KafkaData<T> delete(T data){
        return KafkaData.<T>builder()
                .action(KafkaAction.DELETE)
                .data(data)
                .build();
    }
}
