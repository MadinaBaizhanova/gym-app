package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.error.ErrorDTO;
import com.epam.wca.gym.exception.AuthorizationFailedException;
import com.epam.wca.gym.exception.EntityNotFoundException;
import com.epam.wca.gym.exception.InvalidInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.I_AM_A_TEAPOT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR = "error";
    private static final String EXCEPTION_CAUGHT_TEMPLATE = "Exception caught: %s. Request: %s";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException exception, WebRequest request) {
        logError(exception, request);
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorDTO> handleIllegalStateException(IllegalStateException exception, WebRequest request) {
        logError(exception, request);
        return new ResponseEntity<>(new ErrorDTO(ERROR, exception.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorDTO> handleInvalidInputException(InvalidInputException exception, WebRequest request) {
        logError(exception, request);
        return new ResponseEntity<>(new ErrorDTO(ERROR, exception.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorDTO> handleNoHandlerFoundException(NoHandlerFoundException exception, WebRequest request) {
        logWarn(exception, request);
        return new ResponseEntity<>(
                new ErrorDTO("Resource Not Found", "You are trying to use a non-existing endpoint"),
                NOT_FOUND);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleEntityNotFoundException(EntityNotFoundException exception, WebRequest request) {
        logError(exception, request);
        return new ResponseEntity<>(new ErrorDTO(ERROR, exception.getMessage()), NOT_FOUND);
    }

    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorDTO> handleAuthorizationFailedException(AuthorizationFailedException exception, WebRequest request) {
        logWarn(exception, request);
        return new ResponseEntity<>(new ErrorDTO(ERROR, exception.getMessage()), UNAUTHORIZED);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorDTO> handleSecurityException(SecurityException exception, WebRequest request) {
        logError(exception, request);
        return new ResponseEntity<>(new ErrorDTO(ERROR, exception.getMessage()), FORBIDDEN);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDTO> handleInvalidRequestBody(HttpMessageNotReadableException exception, WebRequest request) {
        logDebug(exception, request);
        return new ResponseEntity<>(
                new ErrorDTO("Invalid Request Body", "Wrong request body used for this endpoint"),
                I_AM_A_TEAPOT);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorDTO> handleMethodNotSupported(HttpRequestMethodNotSupportedException exception, WebRequest request) {
        logError(exception, request);
        return new ResponseEntity<>(
                new ErrorDTO("Invalid Request Method", "Wrong request method for this endpoint"),
                I_AM_A_TEAPOT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleAllOtherExceptions(Exception exception, WebRequest request) {
        logError(exception, request);
        return new ResponseEntity<>(
                new ErrorDTO("Houston, we have a problem!", "Something went wrong with your request. Please check and try again."),
                I_AM_A_TEAPOT);
    }

    private void logError(Exception exception, WebRequest request) {
        String logMessage = EXCEPTION_CAUGHT_TEMPLATE.formatted(exception.getMessage(), request.getDescription(false));
        log.error(logMessage);
    }

    private void logWarn(Exception exception, WebRequest request) {
        String logMessage = EXCEPTION_CAUGHT_TEMPLATE.formatted(exception.getMessage(), request.getDescription(false));
        log.warn(logMessage);
    }

    private void logDebug(Exception exception, WebRequest request) {
        String logMessage = EXCEPTION_CAUGHT_TEMPLATE.formatted(exception.getMessage(), request.getDescription(false));
        log.debug(logMessage);
    }
}