package com.playdata.domain.task.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "task_information")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskInformation {
    @Id
    private UUID id;

    private String content;

    @Enumerated(EnumType.STRING)
    private Period period;

    public static TaskInformation fromId(UUID id){
        return TaskInformation.builder()
                .id(id)
                .build();
    }

    public static TaskInformation createTask(UUID id, String content, Period period){
        return TaskInformation.builder()
                .id(id)
                .period(period)
                .content(content)
                .build();
    }

    @Builder
    public TaskInformation(UUID id, String content, Period period) {
        this.id = id;
        this.content = content;
        this.period = period;
    }
}
