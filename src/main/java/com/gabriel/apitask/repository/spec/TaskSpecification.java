package com.gabriel.apitask.repository.spec;

import com.gabriel.apitask.model.Task;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TaskSpecification {

    public static Specification<Task> byData(LocalDate date) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("date"), date));
    }

}
