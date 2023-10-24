package com.playdata.kafka.dto;

import java.util.List;

public record ArticleKafkaData(
        Long id,
        String title,
        String content,
        List<TaskKafkaData> tasks
) {
}
