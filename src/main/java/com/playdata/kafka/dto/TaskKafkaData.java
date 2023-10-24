package com.playdata.kafka.dto;

import com.playdata.domain.task.entity.Period;

import java.util.UUID;

public record TaskKafkaData(
        UUID id,
        Period period,
        String content
) {
}
