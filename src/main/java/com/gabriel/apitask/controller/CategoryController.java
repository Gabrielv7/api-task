package com.gabriel.apitask.controller;

import com.gabriel.apitask.dto.request.CategoryRequest;
import com.gabriel.apitask.dto.response.CategoryResponse;
import com.gabriel.apitask.mapper.CategoryMapper;
import com.gabriel.apitask.model.Category;
import com.gabriel.apitask.service.CategoryService;
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
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private CategoryService service;
    private CategoryMapper mapper;

    @Operation(summary = "Cadastra categoria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoria salva com sucesso"),
            @ApiResponse(responseCode = "400", description = "Categoria já cadastrada", content = @Content)
    })
    @PostMapping
    public ResponseEntity<CategoryResponse> create(@RequestBody @Valid CategoryRequest categoryRequest) {
        Category categorySaved = service.save(mapper.toEntity(categoryRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(categorySaved));
    }

    @Operation(summary = "Busca todas as categorias")
    @ApiResponse(responseCode = "200")
    @GetMapping
    public ResponseEntity<Page<CategoryResponse>> getAll(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable).map(mapper::toResponse));
    }

    @Operation(summary = "Busca uma categoria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable(value = "id") Integer id) {
        return ResponseEntity.ok(mapper.toResponse(service.findById(id)));
    }

    @Operation(summary = "Atualiza uma categoria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content),
            @ApiResponse(responseCode = "400", description = "Categoria já cadastrada", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable(value = "id") Integer id,
                                                   @RequestBody @Valid CategoryRequest categoryRequest) {

        Category categoryUpdated = service.update(id, mapper.toEntity(categoryRequest));
        return ResponseEntity.ok(mapper.toResponse(categoryUpdated));
    }

    @Operation(summary = "Deleta uma categoria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoria deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content),
            @ApiResponse(responseCode = "409", description = "Categoria em uso", content = @Content)

    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") Integer id) {
        service.deleteById(id);
    }

}
