package com.meli.coupon.infrastructure.exception;

import com.meli.coupon.infrastructure.rest.dto.ErrorDetail;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
        HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
            log.error(String.valueOf(errors));
        });

        return new ResponseEntity<>(new ErrorDetail(new Date(), "Validation request", errors),
            HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ErrorDetail(new Date(), "Conflict", ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<Object> handleNoSuchElementFoundException(
        HttpClientErrorException itemNotFoundException, WebRequest request) {
        log.error(itemNotFoundException.getResponseBodyAsString());
        return new ResponseEntity<>(
            new ErrorDetail(new Date(), "Not Found", itemNotFoundException.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> globalExceptionHandler(Exception ex, WebRequest request) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ErrorDetail(new Date(), "Internal Server Error", ex.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
