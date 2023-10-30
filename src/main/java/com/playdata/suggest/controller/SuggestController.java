package com.playdata.suggest.controller;

import com.playdata.domain.task.response.TaskResponse;
import com.playdata.suggest.service.SuggestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/suggest")
public class SuggestController {

    private final SuggestService suggestService;

    @GetMapping("/question/{questionId}")
    public List<TaskResponse> getByQuestionId(@PathVariable("questionId") Long id){
        return suggestService.getByQuestionId(id);
    }
}
