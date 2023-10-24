package com.playdata.domain.suggest.entity;

import com.playdata.domain.task.entity.TaskInformation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "suggest")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Suggest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long questionId;

    @ManyToOne
    private TaskInformation taskInformation;

    public static Suggest createSuggest(Long questionId, TaskInformation taskInformation){
        return Suggest.builder()
                .questionId(questionId)
                .taskInformation(taskInformation)
                .build();
    }

    @Builder
    public Suggest(Long questionId, TaskInformation taskInformation) {
        this.questionId = questionId;
        this.taskInformation = taskInformation;
    }
}
