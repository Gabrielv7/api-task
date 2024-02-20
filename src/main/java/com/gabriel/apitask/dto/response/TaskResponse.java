package com.gabriel.apitask.dto.response;

import java.time.LocalDate;

public record TaskResponse(Integer id,
                           String description,
                           Boolean completed,
                           LocalDate date) {
}
