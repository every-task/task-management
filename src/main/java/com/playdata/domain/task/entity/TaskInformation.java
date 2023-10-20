package com.playdata.domain.task.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
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
}
