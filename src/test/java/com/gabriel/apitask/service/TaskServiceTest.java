package com.gabriel.apitask.service;

import com.gabriel.apitask.dto.request.TaskRequest;
import com.gabriel.apitask.exception.BusinessException;
import com.gabriel.apitask.exception.NotFoundException;
import com.gabriel.apitask.factory.ScenarioFactory;
import com.gabriel.apitask.mapper.TaskMapper;
import com.gabriel.apitask.model.Category;
import com.gabriel.apitask.model.Task;
import com.gabriel.apitask.repository.TaskRepository;
import com.gabriel.apitask.validator.TaskValidator;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @InjectMocks
    TaskService taskService;

    @Mock
    TaskRepository taskRepository;

    @Mock
    TaskValidator taskValidator;

    @Mock
    CategoryService categoryService;

    @Mock
    MessageSource messageSource;

    @Mock
    TaskMapper taskMapper;

    @Test
    void Dado_uma_tarefa_nao_cadastrada_Quando_buscar_por_id_Entao_deve_lancar_Exception() {
        when(taskRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> taskService.findById(anyInt()));
    }

    @Test
    void Dado_uma_tarefa_cadastrada_Quando_buscar_id_Entao_deve_retornar_a_tarefa() {
        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(ScenarioFactory.newTask()));
        Task task = assertDoesNotThrow(() -> taskService.findById(anyInt()));
        verify(taskRepository, times(1)).findById(anyInt());
        assertNotNull(task);
    }

    @Test
    void Dado_uma_tarefa_cadastrada_Quando_concluir_Entao_deve_retornar_a_tarefa_concluida() {
        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(ScenarioFactory.newTask()));
        Task task = taskService.conclude(anyInt());
        verify(taskRepository, times(1)).findById(anyInt());
        assertTrue(task.getCompleted());
    }

    @Test
    void Dado_uma_tarefa_nao_cadastrada_Quando_concluir_Entao_deve_retornar_Exception() {
        when(taskRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> taskService.conclude(anyInt()));
    }

    @Test
    void Dado_uma_tarefa_cadastrada_Quando_deletar_Entao_deve_deletar_com_sucesso() {
        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(ScenarioFactory.newTask()));
        taskService.deleteById(anyInt());
        verify(taskRepository, times(1)).findById(anyInt());
        verify(taskRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void Dado_uma_tarefa_nao_cadastrada_Quando_deletar_Entao_deve_lancar_Exception() {
        when(taskRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> taskService.deleteById(anyInt()));
        verify(taskRepository, never()).deleteById(anyInt());
    }

    @Test
    void Dado_uma_lista_de_tarefas_cadastradas_Quando_buscar_por_data_Entao_deve_retornar_a_lista_paginada() {
        when(taskRepository.findAll((Specification<Task>) any(), (Pageable) any())).thenReturn(ScenarioFactory.newTaskPage());
        Page<Task> allByData = taskService.findAllByData(any(Pageable.class), any());
        assertAll(() -> assertNotNull(allByData.getContent()),
                () -> assertFalse(allByData.getContent().isEmpty())
        );
    }

    @Test
    void Dado_uma_nova_tarefa_valida_Quando_criar_Entao_deve_retornar_a_tarefa_salva() {
        Task task = ScenarioFactory.newMappedTask();
        when(taskMapper.toEntity(any(TaskRequest.class))).thenReturn(task);
        when(categoryService.findById(anyInt())).thenReturn(ScenarioFactory.newCategoryWithoutTasks());
        when(taskRepository.save(any(Task.class))).thenReturn(new Task(1, "test", false, LocalDate.now(),
                ScenarioFactory.newCategoryWithTasksDynamicParam(task), LocalDate.now()));

        Task taskSaved = taskService.save(ScenarioFactory.newTaskRequest());

        verify(taskMapper, times(1)).toEntity(any(TaskRequest.class));
        verify(categoryService, times(1)).findById(anyInt());
        verify(taskValidator, times(1)).validateTaskAlreadyExists(any(Task.class), any(Category.class));
        verify(taskRepository, times(1)).save(any(Task.class));

        assertNotNull(taskSaved);
        assertNotNull(taskSaved.getCategory());
        assertEquals("test", taskSaved.getDescription());
    }

    @Test
    void Dado_uma_nova_tarefa_com_uma_categoria_inexistente_Quando_criar_Entao_deve_lancar_Exception() {
        Task task = ScenarioFactory.newMappedTask();
        when(taskMapper.toEntity(any(TaskRequest.class))).thenReturn(task);
        when(categoryService.findById(anyInt())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> taskService.save(ScenarioFactory.newTaskRequest()));

        verify(taskMapper, times(1)).toEntity(any(TaskRequest.class));
        verify(categoryService, times(1)).findById(anyInt());
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void Dado_uma_nova_tarefa_que_ja_existe_Quando_criar_Entao_deve_lancar_Exception() {
        Task task = ScenarioFactory.newMappedTask();
        when(taskMapper.toEntity(any(TaskRequest.class))).thenReturn(task);
        when(categoryService.findById(anyInt())).thenReturn(ScenarioFactory.newCategoryWithTasksDynamicParam(task));
        doThrow(new BusinessException("teste")).when(taskValidator).validateTaskAlreadyExists(any(Task.class), any(Category.class));

        assertThrows(BusinessException.class, () -> taskService.save(ScenarioFactory.newTaskRequest()));

        verify(taskMapper, times(1)).toEntity(any(TaskRequest.class));
        verify(categoryService, times(1)).findById(anyInt());
        verify(taskValidator, times(1)).validateTaskAlreadyExists(any(Task.class), any(Category.class));
        verify(taskRepository, never()).save(any(Task.class));
    }


}