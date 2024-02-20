package com.gabriel.apitask.validator;

import com.gabriel.apitask.exception.BusinessException;
import com.gabriel.apitask.factory.ScenarioFactory;
import com.gabriel.apitask.model.Category;
import com.gabriel.apitask.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class CategoryValidatorTest {

    @InjectMocks
    private CategoryValidator validator;

    @Mock
    private CategoryRepository repository;

    @Mock
    private MessageSource messageSource;

    private Category category;

    @BeforeEach
    void setUp() {
        this.category = ScenarioFactory.newCategory();
    }

    @Test
    void Dado_uma_categoria_ja_cadastra_Quando_criar_Entao_deve_lancar_Exception() {
        when(repository.findByName(anyString())).thenReturn(Optional.of(category));
        when(messageSource.getMessage(any(), any(), any())).thenReturn(anyString());

        assertThrows(BusinessException.class, () -> validator.validateCategoryAlreadyExists(category));

        verify(repository, times(1)).findByName(anyString());
        verify(messageSource, times(1)).getMessage(any(), any(), any());
    }

    @Test
    void Dado_uma_categoria_nao_cadastrada_Quando_criar_Entao_nao_deve_lancar_Exception() {
        when(repository.findByName(anyString())).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> validator.validateCategoryAlreadyExists(ScenarioFactory.newCategory()));
    }

}