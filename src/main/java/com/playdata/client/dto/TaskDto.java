package com.playdata.client.dto;

import com.playdata.domain.task.entity.Period;

import java.util.UUID;

public record TaskDto(
        UUID id,
        Period period,
        String content
) {
}
