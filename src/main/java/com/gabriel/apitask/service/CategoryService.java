package com.gabriel.apitask.service;

import com.gabriel.apitask.exception.ConflictException;
import com.gabriel.apitask.exception.NotFoundException;
import com.gabriel.apitask.model.Category;
import com.gabriel.apitask.model.Task;
import com.gabriel.apitask.repository.CategoryRepository;
import com.gabriel.apitask.validator.CategoryValidator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private CategoryValidator validator;

    @Autowired
    private MessageSource messageSource;

    @Transactional
    public Category save(Category category) {
        validator.validateCategoryAlreadyExists(category);
        return repository.save(category);
    }

    public Page<Category> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Category findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(()-> new NotFoundException(
                   messageSource.getMessage("category.not.found", null, LocaleContextHolder.getLocale())
                ));
    }

    @Transactional
    public Category update(Integer id, Category category) {
        Category categoryFound = findById(id);
        validator.validateCategoryAlreadyExists(category);
        categoryFound.setName(category.getName());
        return categoryFound;
    }

    public void  deleteById(Integer id) {
        Category category = findById(id);
        if (tasksIsNotEmpty(category.getTasks())) {
            throw new ConflictException(messageSource.getMessage("category.in.use", null, LocaleContextHolder.getLocale()));
        }
        repository.deleteById(id);
    }

    private boolean tasksIsNotEmpty(List<Task> tasks) {
        return !tasks.isEmpty();
    }

}
