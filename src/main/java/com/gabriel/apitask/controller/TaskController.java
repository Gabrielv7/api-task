package com.gabriel.apitask.controller;

import com.gabriel.apitask.dto.request.TaskRequest;
import com.gabriel.apitask.dto.response.TaskResponse;
import com.gabriel.apitask.mapper.TaskMapper;
import com.gabriel.apitask.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@AllArgsConstructor
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private TaskService service;
    private TaskMapper mapper;

    @Operation(summary = "Cadastra tarefa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tarefa salva com sucesso"),
            @ApiResponse(responseCode = "404", description = "Categoria da tarefa não existe", content = @Content),
            @ApiResponse(responseCode = "400", description = "Tarefa já cadastrada", content = @Content)

    })
    @PostMapping
    public ResponseEntity<TaskResponse> create(@RequestBody @Valid TaskRequest taskRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(service.save(taskRequest)));
    }

    @Operation(summary = "Busca todas tarefas pela data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "Data não informada", content = @Content)
    })
    @GetMapping
    public ResponseEntity<Page<TaskResponse>> getAllByDate(@PageableDefault Pageable pageable,
                                                           @RequestParam(value = "date") LocalDate date) {
        return ResponseEntity.ok(service.findAllByData(pageable, date).map(mapper::toResponse));
    }

    @Operation(summary = "Busca uma tarefa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa encontrada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getById(@PathVariable(value = "id") Integer id) {
        return ResponseEntity.ok(mapper.toResponse(service.findById(id)));
    }

    @Operation(summary = "Conclui uma tarefa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada", content = @Content),
    })
    @PatchMapping("/{id}/conclude")
    public ResponseEntity<TaskResponse> conclude(@PathVariable(value = "id") Integer id) {
        return ResponseEntity.ok(mapper.toResponse(service.conclude(id)));
    }

    @Operation(summary = "Deletar tarefa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tarefa deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada", content = @Content),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
