package com.gabriel.apitask.exception.handle.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Getter
@Setter
public class ErrorDTO {

    private int status;
    private String message;
    List<ErrorField> errors;

}
