package com.gabriel.apitask.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TaskRequest(@NotBlank String description,
                          @NotNull LocalDate date,
                          @NotNull Integer categoryId) {

}
