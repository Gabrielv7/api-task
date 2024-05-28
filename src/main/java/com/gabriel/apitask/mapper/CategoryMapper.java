package com.gabriel.apitask.mapper;

import com.gabriel.apitask.dto.request.CategoryRequest;
import com.gabriel.apitask.dto.response.CategoryResponse;
import com.gabriel.apitask.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequest categoryRequest) {
        return Category.builder()
                .name(categoryRequest.name())
                .build();
    }

    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }

}
