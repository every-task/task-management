package com.playdata.task;

import com.playdata.client.api.SuccessStoryClient;
import com.playdata.client.response.ArticleResponse;
import com.playdata.domain.task.entity.TaskInformation;
import com.playdata.domain.task.repository.TaskInformationRepository;
import com.playdata.kafka.dto.ArticleKafkaData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskInformationRepository taskInformationRepository;
    private final SuccessStoryClient successStoryClient;

    public void taskRegister(ArticleKafkaData data){
        ArticleResponse articleResponse = successStoryClient.getById(data.id());

        List<TaskInformation> taskInformations = articleResponse.tasks().stream()
                .map(task -> TaskInformation.createTask(
                        task.id(),
                        task.content(),
                        task.period()))
                .toList();

        taskInformationRepository.saveAll(taskInformations);
    }
}
