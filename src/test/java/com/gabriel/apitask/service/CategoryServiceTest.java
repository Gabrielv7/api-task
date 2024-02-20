package com.gabriel.apitask.service;

import com.gabriel.apitask.exception.ConflictException;
import com.gabriel.apitask.exception.NotFoundException;
import com.gabriel.apitask.factory.ScenarioFactory;
import com.gabriel.apitask.model.Category;
import com.gabriel.apitask.repository.CategoryRepository;
import com.gabriel.apitask.validator.CategoryValidator;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService service;

    @Mock
    private CategoryRepository repository;

    @Mock
    private CategoryValidator validator;

    @Mock
    private MessageSource messageSource;

    @Test
    void Dado_uma_lista_de_categorias_cadastrada_Quando_buscar_Entao_deve_retornar_a_lista_de_categorias_paginada() {
        when(repository.findAll((Pageable) any())).thenReturn(ScenarioFactory.newCategoriesPage());
        Page<Category> allByData = service.findAll(any(Pageable.class));
        assertAll(() -> assertNotNull(allByData.getContent()),
                () -> assertFalse(allByData.getContent().isEmpty()));
    }

    @Test
    void Dado_uma_categoria_cadastrada_Quando_buscar_por_id_Entao_deve_retornar_a_categoria() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(ScenarioFactory.newCategory()));
        Category category = service.findById(anyInt());
        verify(repository, times(1)).findById(anyInt());
        assertNotNull(category);
    }

    @Test
    void Dado_uma_categoria_nao_cadastrada_Quando_buscar_por_id_Entao_deve_lancar_Exception() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.findById(anyInt()));
    }

    @Test
    void Dado_uma_categoria_sem_tarefa_cadastrada_Quando_deletar_Entao_deve_deletar_com_sucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(ScenarioFactory.newCategoryWithoutTasks()));
        service.deleteById(anyInt());
        verify(repository, times(1)).findById(anyInt());
        verify(repository, times(1)).deleteById(anyInt());
    }

    @Test
    void Dado_uma_categoria_com_tarefa_cadastrada_Quando_deletar_Entao_deve_lancar_Exception() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(ScenarioFactory.newCategoryWithTasks()));
        assertThrows(ConflictException.class, () -> service.deleteById(anyInt()));
        verify(repository, times(1)).findById(anyInt());
        verify(repository, never()).deleteById(anyInt());
    }

    @Test
    void Dado_uma_categoria_nao_cadastrada_Quando_deletar_Entao_deve_lancar_Exception() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.deleteById(anyInt()));
    }

    @Test
    void Dado_uma_categoria_valida_Quando_cadastrar_Entao_deve_retornar_a_categoria_salva() {
        when(repository.save(any(Category.class))).thenReturn(ScenarioFactory.newCategory());
        Category category = service.save(ScenarioFactory.newCategoryWithoutId());
        verify(repository, times(1)).save(any(Category.class));
        assertNotNull(category);
        assertEquals("test", category.getName());
    }

    @Test
    void Dado_uma_atualizacao_de_categoria_com_id_inexistente_Quando_atualizar_Entao_deve_lancar_exception() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () ->  service.update(1, ScenarioFactory.newCategory()));
    }

    @Test
    void Dado_uma_atualizacao_de_categoria_valida_Quando_atualizar_Entao_deve_retornar_a_categoria_atualizada() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(ScenarioFactory.newCategory()));
        Category CategoryUpdated = service.update(1, ScenarioFactory.newCategoryDynamicName("testUpdate"));
        verify(repository, times(1)).findById(anyInt());
        assertEquals("testUpdate", CategoryUpdated.getName());
    }

}