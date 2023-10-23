package com.playdata.client.response;

import com.playdata.client.dto.TaskDto;

import java.util.List;

public record ArticleResponse(
        Long id,
        String content,
        List<TaskDto> tasks
) {
}
