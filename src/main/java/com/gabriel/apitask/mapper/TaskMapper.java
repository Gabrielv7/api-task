package com.gabriel.apitask.mapper;

import com.gabriel.apitask.dto.request.TaskRequest;
import com.gabriel.apitask.dto.response.TaskResponse;
import com.gabriel.apitask.model.Task;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskMapper {

    public Task toEntity(TaskRequest taskRequest) {
        return Task.builder()
                .description(taskRequest.description())
                .date(taskRequest.date())
                .build();
    }

    public TaskResponse toResponse(Task task) {
        return new TaskResponse(task.getId(), task.getDescription(), task.getCompleted(), task.getDate());
    }

}
