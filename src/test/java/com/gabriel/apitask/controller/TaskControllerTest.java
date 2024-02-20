package com.gabriel.apitask.controller;

import com.gabriel.apitask.dto.request.TaskRequest;
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
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    TaskRepository taskRepository;

    private Category category;

    private Task task;

    @BeforeEach
    void setUp() {
        category = categoryRepository.save(Category.builder()
                .name("test")
                .build());

        task = taskRepository.save(Task.builder()
                .description("test test")
                .completed(false)
                .date(LocalDate.now())
                .category(category)
                .build());
    }

    @Order(1)
    @Test
    void Dado_uma_nova_terefa_valida_Quando_criar_Entao_deve_retornar_status_http_201() throws Exception {
        TaskRequest taskRequest = new TaskRequest("test", LocalDate.now(), category.getId());

        mockMvc.perform(
                post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(taskRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("test"))
                .andExpect(jsonPath("$.completed").value(false))
                .andExpect(jsonPath("$.date").value(String.valueOf(LocalDate.now())));

    }

    @Order(2)
    @Test
    void Dado_uma_nova_terefa_com_categoria_sem_cadastro_Quando_criar_Entao_deve_retornar_status_http_404() throws Exception {
        Integer categoryIdNotExist = 0;
        TaskRequest taskRequest = new TaskRequest("test", LocalDate.now(), categoryIdNotExist);
        mockMvc.perform(
                        post("/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertToJson(taskRequest)))
                .andExpect(status().isNotFound());

    }

    @Order(3)
    @Test
    void Dado_uma_tarefa_que_ja_esta_cadastrada_Quando_criar_Entao_deve_retornar_status_http_400() throws Exception {

        TaskRequest taskRequest = new TaskRequest("test test", LocalDate.now(), category.getId());

        mockMvc.perform(
                        post("/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertToJson(taskRequest)))
                .andExpect(status().isBadRequest());

    }

    @Order(4)
    @Test
    void Dado_uma_lista_de_tarefas_Quando_buscar_por_data_Entao_deve_retornar_status_http_200() throws Exception {
        mockMvc.perform(get("/tasks?date={date}", LocalDate.now()))
                .andExpect(status().isOk());

    }

    @Order(5)
    @Test
    void Dado_uma_lista_de_tarefas_Quando_nao_buscar_por_data_Entao_deve_retornar_status_http_400() throws Exception {
        mockMvc.perform(get("/tasks", LocalDate.now()))
                .andExpect(status().isBadRequest());

    }


    @Order(6)
    @Test
    void Dado_uma_tarefa_cadastrada_Quando_buscar_Entao_deve_retornar_status_http_200() throws Exception {
        mockMvc.perform(get("/tasks/{id}", task.getId()))
                .andExpect(status().isOk());
    }

    @Order(7)
    @Test
    void Dado_uma_tarefa_nao_cadastrada_Quando_buscar_Entao_deve_retornar_status_http_404() throws Exception {
        Integer idNotExists = 0;
        mockMvc.perform(get("/taks/{id}", idNotExists))
                .andExpect(status().isNotFound());
    }

    @Order(8)
    @Test
    void Dado_uma_tarefa_cadastrada_Quando_atualizar_Entao_deve_retornar_status_http_200() throws Exception {
        Integer idExists = task.getId();
        mockMvc.perform(patch("/tasks/{id}/conclude", idExists))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idExists))
                .andExpect(jsonPath("$.completed").value(true));

    }

    @Order(9)
    @Test
    void Dado_uma_tarefa_nao_cadastrada_Quando_atualizar_Entao_deve_retornar_status_http_404() throws Exception {
        Integer idNotExists = 0;
        mockMvc.perform(patch("/tasks/{id}/conclude", idNotExists))
                .andExpect(status().isNotFound());

    }

    @Order(10)
    @Test
    void Dado_uma_tarefa_cadastrada_Quando_deletar_Entao_deve_retornar_status_http_204() throws Exception {
        mockMvc.perform(delete("/tasks/{id}", task.getId()))
                .andExpect(status().isNoContent());
    }

    @Order(11)
    @Test
    void Dado_uma_tarefa_nao_cadastrada_Quando_deletar_Entao_deve_retornar_status_http_404() throws Exception {
        Integer idNotExists = 0;
        mockMvc.perform(delete("/tasks/{id}", idNotExists))
                .andExpect(status().isNotFound());
    }

    @Order(12)
    @Test
    void Dado_uma_nova_tarefa_com_campos_invalidos_Quando_criar_Entao_deve_retornar_status_http_201() throws Exception {
        TaskRequest invalidTaskRequest = new TaskRequest("", null, null);

        mockMvc.perform(
                        post("/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertToJson(invalidTaskRequest)))
                .andExpect(status().isBadRequest());
    }

}