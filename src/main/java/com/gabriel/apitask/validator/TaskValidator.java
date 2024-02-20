package com.gabriel.apitask.validator;

import com.gabriel.apitask.exception.BusinessException;
import com.gabriel.apitask.model.Category;
import com.gabriel.apitask.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class TaskValidator {

    @Autowired
    private MessageSource messageSource;

    public void validateTaskAlreadyExists(Task task, Category category) {
        boolean alreadyExists = category.getTasks()
                .stream().anyMatch(t -> t.getDescription().equals(task.getDescription()) &&
                        t.getDate().equals(task.getDate()));

        if (alreadyExists) {
            String messageError = messageSource.getMessage("task.already.registered", null, LocaleContextHolder.getLocale());
            throw new BusinessException(String.format(messageError, task.getDescription()));
        }
    }

}
