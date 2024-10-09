package com.epam.wca.gym.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    public static final String ERROR = "error";
    public static final String MESSAGE = "message";
    public static final String EXCEPTION_CAUGHT = "Exception caught: {}. Request: {}";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e,
                                                                          WebRequest request) {
        log.error(EXCEPTION_CAUGHT, e.getMessage(), request.getDescription(false));
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public ResponseEntity<Map<String, String>> handleMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                                        WebRequest request) {
        log.error(EXCEPTION_CAUGHT, e.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(Map.of(
                ERROR, "Damn! Wrong HTTP request method.",
                MESSAGE, "You tried to use an unsupported method for this endpoint."
        ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public ResponseEntity<Map<String, String>> handleInvalidRequestBody(HttpMessageNotReadableException e,
                                                                        WebRequest request) {
        log.error(EXCEPTION_CAUGHT, e.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(Map.of(
                ERROR, "Damn! Invalid request body.",
                MESSAGE, "Wrong request body used for this endpoint."
        ));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNoHandlerFoundException(NoHandlerFoundException e, WebRequest request) {
        log.error(EXCEPTION_CAUGHT, e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>("Damn! Resource not found. You are trying to use a non-existing endpoint",
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleSecurityException(SecurityException e, WebRequest request) {
        log.error(EXCEPTION_CAUGHT, e.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFoundException(EntityNotFoundException e,
                                                                             WebRequest request) {
        log.error(EXCEPTION_CAUGHT, e.getMessage(), request.getDescription(false));
        Map<String, String> errors = new HashMap<>();
        errors.put(ERROR, e.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalStateException e, WebRequest request) {
        log.error(EXCEPTION_CAUGHT, e.getMessage(), request.getDescription(false));
        Map<String, String> errors = new HashMap<>();
        errors.put(ERROR, e.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<Map<String, String>> handleInvalidInputException(InvalidInputException e, WebRequest request) {
        log.error(EXCEPTION_CAUGHT, e.getMessage(), request.getDescription(false));
        Map<String, String> errors = new HashMap<>();
        errors.put(ERROR, e.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public ResponseEntity<Map<String, String>> handleAllOtherExceptions(Exception e, WebRequest request) {
        log.error(EXCEPTION_CAUGHT, e.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(Map.of(
                ERROR, "Houston, we have a problem!",
                MESSAGE, "Something went wrong with your request. Please check and try again."
        ));
    }
}