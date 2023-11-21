package com.playdata.kafka.dto;

import com.playdata.domain.task.entity.ArticleCategory;

public record QuestionKafkaData(
        Long id,
        String title,
        String content,
        ArticleCategory category
) {
}
