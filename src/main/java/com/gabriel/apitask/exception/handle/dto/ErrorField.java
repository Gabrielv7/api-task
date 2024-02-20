package com.gabriel.apitask.exception.handle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorField {
    private String field;
    private String message;
}
