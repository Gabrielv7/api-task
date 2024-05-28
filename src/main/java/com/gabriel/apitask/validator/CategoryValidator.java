package com.gabriel.apitask.validator;

import com.gabriel.apitask.exception.BusinessException;
import com.gabriel.apitask.model.Category;
import com.gabriel.apitask.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CategoryValidator {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MessageSource messageSource;

    public void validateCategoryAlreadyExists(Category category) {
        if (categoryAlreadyExist(category)) {
            String messageError = messageSource.getMessage("category.already.registered", null, LocaleContextHolder.getLocale());
            throw new BusinessException(String.format(messageError, category.getName()));
        }
    }

    private boolean categoryAlreadyExist(Category category) {
        return categoryRepository.existsByName(category.getName());
    }
}
