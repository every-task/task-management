package com.playdata.kafka.dto;

import com.playdata.domain.task.entity.ArticleCategory;
import lombok.Builder;

import java.util.List;

@Builder
public record ArticleKafkaData(
        Long id,
        String title,
        String content,
        ArticleCategory category,
        List<TaskKafkaData> tasks
) {
}
