package com.gabriel.apitask.exception.handle;

import com.gabriel.apitask.exception.BusinessException;
import com.gabriel.apitask.exception.ConflictException;
import com.gabriel.apitask.exception.NotFoundException;
import com.gabriel.apitask.exception.handle.dto.ErrorDTO;
import com.gabriel.apitask.exception.handle.dto.ErrorField;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDTO> handleBusinessException(BusinessException ex) {
        ErrorDTO errorDTO = buildErrorDTO(BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(errorDTO);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDTO> handleBusinessException(NotFoundException ex) {
        ErrorDTO errorDTO = buildErrorDTO(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorDTO> handleConflictException(ConflictException ex) {
        ErrorDTO errorDTO = buildErrorDTO(HttpStatus.CONFLICT.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDTO);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {

        List<ErrorField> fieldErrors = new ArrayList<>();

        for (FieldError field : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.add(new ErrorField(field.getField(), field.getDefaultMessage()));
        }

        ErrorDTO errorDTO = buildErrorsValidationResponse(BAD_REQUEST, "Request invalid", fieldErrors);
        return ResponseEntity.badRequest().body(errorDTO);
    }

    private ErrorDTO buildErrorsValidationResponse(HttpStatus httpStatus, String message, List<ErrorField> errorFields) {
        return ErrorDTO.builder()
                .status(httpStatus.value())
                .message(message)
                .errors(errorFields)
                .build();
    }

    private ErrorDTO buildErrorDTO(int status, String message) {
        return ErrorDTO.builder()
                .status(status)
                .message(message)
                .build();
    }

}
