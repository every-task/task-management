package com.playdata.client.story.response;

import com.playdata.client.story.dto.TaskDto;

import java.util.List;

public record ArticleResponse(
        Long id,
        String content,
        List<TaskDto> tasks
) {
}
