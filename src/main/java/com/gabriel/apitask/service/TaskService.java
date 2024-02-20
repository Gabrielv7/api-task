package com.gabriel.apitask.service;

import com.gabriel.apitask.dto.request.TaskRequest;
import com.gabriel.apitask.exception.NotFoundException;
import com.gabriel.apitask.mapper.TaskMapper;
import com.gabriel.apitask.model.Category;
import com.gabriel.apitask.model.Task;
import com.gabriel.apitask.repository.TaskRepository;
import com.gabriel.apitask.repository.spec.TaskSpecification;
import com.gabriel.apitask.validator.TaskValidator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskValidator validator;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private TaskMapper mapper;

    @Transactional
    public Task save(TaskRequest taskRequest) {
        Task task = mapper.toEntity(taskRequest);
        Category category = categoryService.findById(taskRequest.categoryId());
        validator.validateTaskAlreadyExists(task, category);
        task.setCategory(category);
        return taskRepository.save(task);
    }

    public Page<Task> findAllByData(Pageable pageable, LocalDate date) {
        return taskRepository.findAll(Specification.where(TaskSpecification.byData(date)), pageable);
    }

    public Task findById(Integer id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        messageSource.getMessage("task.not.found", null, LocaleContextHolder.getLocale())
                ));
    }

    @Transactional
    public Task conclude(Integer id) {
        Task task = findById(id);
        task.setCompleted(true);
        return task;
    }

    public void deleteById(Integer id) {
        findById(id);
        taskRepository.deleteById(id);
    }

}
