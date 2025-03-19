package com.example.note.exception;

import com.example.note.response.ResponseApi;
import com.example.note.response.ResponseCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ResponseApi<String> handleNotFoundException(NotFoundException ex) {
        return new ResponseApi<>(null, ResponseCode.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseApi<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseApi<>(null, ResponseCode.BAD_REQUEST, String.valueOf(errors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseApi<String> handleConstraintViolation(ConstraintViolationException ex) {
        StringBuilder errorMessage = new StringBuilder();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();

        for (ConstraintViolation<?> violation : violations) {
            errorMessage.append(violation.getPropertyPath())
                    .append(" - ")
                    .append(violation.getMessage())
                    .append("\n");
        }

        return new ResponseApi<>(null, ResponseCode.BAD_REQUEST, String.valueOf(errorMessage));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseApi<String> handleException(NotFoundException ex) {
        return new ResponseApi<>(null, ResponseCode.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

}
