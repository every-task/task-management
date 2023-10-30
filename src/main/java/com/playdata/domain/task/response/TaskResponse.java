package com.playdata.domain.task.response;

import com.playdata.domain.task.entity.Period;
import com.playdata.domain.task.entity.TaskInformation;

import java.util.UUID;

public record TaskResponse(
        UUID id,
        Period period,
        String content
) {
    public static TaskResponse fromEntity(TaskInformation taskInformation){
        return new TaskResponse(
                taskInformation.getId(),
                taskInformation.getPeriod(),
                taskInformation.getContent()
        );
    }
}
