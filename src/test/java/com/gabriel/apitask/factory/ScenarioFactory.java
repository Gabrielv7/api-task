package com.gabriel.apitask.factory;

import com.gabriel.apitask.dto.request.TaskRequest;
import com.gabriel.apitask.model.Category;
import com.gabriel.apitask.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class ScenarioFactory {

    private ScenarioFactory() {}

    public static Category newCategory() {
        return Category.builder()
                .id(1)
                .name("test")
                .build();
    }

    public static Category newCategoryWithTasks() {
        return Category.builder()
                .id(1)
                .name("test")
                .tasks(Collections.singletonList(new Task(1, "task test", false,
                        LocalDate.now(), newCategory(), LocalDate.now())))
                .build();
    }

    public static Category newCategoryWithTasksDynamicParam(Task task) {
        return Category.builder()
                .id(1)
                .name("test")
                .tasks(Collections.singletonList(task))
                .build();
    }

    public static Category newCategoryWithoutTasks() {
        return Category.builder()
                .id(1)
                .name("test")
                .tasks(Collections.EMPTY_LIST)
                .build();
    }

    public static Category newCategoryDynamicName(String name) {
        return Category.builder()
                .id(1)
                .name(name)
                .build();
    }

    public static Category newCategoryWithoutId() {
        return Category.builder()
                .name("test")
                .build();
    }

    public static Page<Category> newCategoriesPage() {
        return new PageImpl<>(List.of(newCategory()));
    }

    public static Task newTask(Category category) {
        return new Task(1, "teste 1", false, LocalDate.now(), category, LocalDate.now());
    }

    public static Task newTask() {
        return Task.builder()
                .id(1)
                .description("teste 1")
                .completed(false)
                .date(LocalDate.now())
                .build();
    }

    public static Page<Task> newTaskPage() {
        return new PageImpl<>(List.of(newTask()));
    }

    public static TaskRequest newTaskRequest() {
        return new TaskRequest("teste", LocalDate.now(), 1);
    }

    public static Task newMappedTask() {
        return Task.builder()
                .description("test")
                .date(LocalDate.now())
                .build();
    }

}
