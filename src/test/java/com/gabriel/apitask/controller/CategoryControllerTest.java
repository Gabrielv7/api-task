package com.gabriel.apitask.controller;

import com.gabriel.apitask.dto.request.CategoryRequest;
import com.gabriel.apitask.model.Category;
import com.gabriel.apitask.model.Task;
import com.gabriel.apitask.repository.CategoryRepository;
import com.gabriel.apitask.repository.TaskRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static com.gabriel.apitask.utils.Converter.convertToJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    TaskRepository taskRepository;

    private Category category;

    @BeforeEach
    void setUp() {
        category = categoryRepository.save(Category.builder().name("test test").build());
    }

    @AfterEach
    void afterEach() {
        categoryRepository.deleteById(category.getId());
    }

    @Order(1)
    @Test
    void Dado_uma_nova_categoria_valida_Quando_criar_Entao_deve_retornar_status_http_201() throws Exception {
        CategoryRequest newCategory = new CategoryRequest("test2");
        mockMvc.perform(
                post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(newCategory)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("test2"));
    }

    @Order(2)
    @Test
    void Dado_uma_categoria_cadastrada_Quando_criar_Entao_deve_retornar_status_http_400() throws Exception {
        CategoryRequest categoryAlreadyExists = new CategoryRequest("test2");
        mockMvc.perform(
                        post("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertToJson(categoryAlreadyExists)))
                .andExpect(status().isBadRequest());
    }


    @Order(3)
    @Test
    void Dado_uma_lista_de_categorias_Quando_buscar_Entao_deve_retonar_status_http_200() throws Exception {
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk());
    }

    @Order(4)
    @Test
    void Dado_uma_categoria_cadastrada_Quando_buscar_Entao_deve_retornar_status_http_200() throws Exception {
        Integer idExists = category.getId();
        mockMvc.perform(get("/categories/{id}", idExists))
                .andExpect(status().isOk());

    }

    @Order(5)
    @Test
    void Dado_uma_categoria_nao_cadastrada_Quando_buscar_Entao_deve_retonar_status_http_404() throws Exception {
         Integer idNotExists = 0;
        mockMvc.perform(get("/categories/{id}", idNotExists))
                .andExpect(status().isNotFound());
    }

    @Order(6)
    @Test
    void Dado_uma_categoria_cadastrada_Quando_atualizar_Entao_deve_retornar_status_http_200() throws Exception {
        Integer idExists = category.getId();
        mockMvc.perform(put("/categories/{id}", idExists)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(new CategoryRequest("testUpdate"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idExists))
                .andExpect(jsonPath("$.name").value("testUpdate"));

    }

    @Order(7)
    @Test
    void Dado_uma_categoria_nao_cadastrada_Quando_atualizar_Entao_deve_retornar_status_http_404() throws Exception {
        Integer idNotExists = 0;
        mockMvc.perform(put("/categories/{id}", idNotExists)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(new CategoryRequest("testUpdate"))))
                .andExpect(status().isNotFound());

    }

    @Order(8)
    @Test
    void Dado_uma_categoria_cadastrada_Quando_atualizar_Entao_deve_retornar_status_http_400() throws Exception {
        Integer idExists = category.getId();
        CategoryRequest categoryAlreadyExists = new CategoryRequest("test2");
        mockMvc.perform(put("/categories/{id}", idExists)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(categoryAlreadyExists)))
                .andExpect(status().isBadRequest());

    }

    @Order(9)
    @Test
    void Dado_uma_categoria_cadastrada_e_sem_relacionamento_Quando_deletar_Entao_deve_retornar_status_http_204() throws Exception {
        Integer idExists = category.getId();
        mockMvc.perform(delete("/categories/{id}", idExists))
                .andExpect(status().isNoContent());
    }

    @Order(10)
    @Test
    void Dado_uma_categoria_nao_cadastrada_Quando_deletar_Entao_deve_retornar_status_http_404() throws Exception {
        Integer idNotExists = 0;
        mockMvc.perform(delete("/categories/{id}", idNotExists))
                .andExpect(status().isNotFound());
    }

    @Order(11)
    @Test
    void Dado_uma_categoria_cadastrada_com_relacionamento_Quando_deletar_Entao_deve_retornar_status_http_409() throws Exception {
        Category newCategory = categoryRepository.save(Category.builder().name("test").build());

        taskRepository.save(Task.builder()
                .description("test")
                .completed(false)
                .date(LocalDate.now())
                .category(newCategory)
                .build());

        Integer idExists = newCategory.getId();

        mockMvc.perform(delete("/categories/{id}", idExists))
                .andExpect(status().isConflict());
    }

    @Order(12)
    @Test
    void Dado_uma_nova_categoria_com_campos_invalidos_Quando_criar_Entao_deve_retornar_status_http_400() throws Exception {
        CategoryRequest invalidCategory = new CategoryRequest("");
        mockMvc.perform(
                        post("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertToJson(invalidCategory)))
                .andExpect(status().isBadRequest());
    }


}