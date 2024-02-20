package com.gabriel.apitask.validator;

import com.gabriel.apitask.exception.BusinessException;
import com.gabriel.apitask.factory.ScenarioFactory;
import com.gabriel.apitask.model.Category;
import com.gabriel.apitask.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class TaskValidatorTest {

    @InjectMocks
    private TaskValidator taskValidator;

    @Mock
    private MessageSource messageSource;

    private Category category;
    private Task task;

    @BeforeEach
    void setUp() {
        category = ScenarioFactory.newCategoryWithoutTasks();
        task = ScenarioFactory.newTask(category);
        category.setTasks(Collections.singletonList(task));
    }

    @Test
    void Dado_uma_tarefa_ja_cadastrada_Quando_criar_Entao_deve_lancar_Exception() {
        when(messageSource.getMessage(any(), any(), any())).thenReturn("Tarefa %s já está cadastrada.");
        assertThrows(BusinessException.class, () -> taskValidator.validateTaskAlreadyExists(task, category));
    }

    @Test
    void Dado_uma_tarefa_nao_cadastrada_Quando_criar_Entao_nao_deve_lancar_Exception() {
        Task newTask = new Task(1, "teste 2", false, LocalDate.now(), category, LocalDate.now());
        assertDoesNotThrow(() -> taskValidator.validateTaskAlreadyExists(newTask, category));
    }

}