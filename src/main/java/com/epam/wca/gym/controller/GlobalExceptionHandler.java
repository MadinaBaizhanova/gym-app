package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.error.ErrorDTO;
import com.epam.wca.gym.exception.AuthorizationFailedException;
import com.epam.wca.gym.exception.EntityNotFoundException;
import com.epam.wca.gym.exception.InvalidInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
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
import static org.springframework.http.HttpStatus.LOCKED;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR = "error";
    private static final String EXCEPTION_CAUGHT_TEMPLATE = "Exception caught: %s. Request: %s";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException exception, WebRequest request) {
        logError(exception, request);

        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity
                .status(BAD_REQUEST)
                .contentType(APPLICATION_JSON)
                .body(errors);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorDTO> handleIllegalStateException(IllegalStateException exception, WebRequest request) {
        logError(exception, request);

        return ResponseEntity
                .status(BAD_REQUEST)
                .contentType(APPLICATION_JSON)
                .body(new ErrorDTO(ERROR, exception.getMessage()));
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorDTO> handleInvalidInputException(InvalidInputException exception, WebRequest request) {
        logError(exception, request);

        return ResponseEntity
                .status(BAD_REQUEST)
                .contentType(APPLICATION_JSON)
                .body(new ErrorDTO(ERROR, exception.getMessage()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorDTO> handleNoHandlerFoundException(NoHandlerFoundException exception, WebRequest request) {
        logWarn(exception, request);

        return ResponseEntity
                .status(NOT_FOUND)
                .contentType(APPLICATION_JSON)
                .body(new ErrorDTO("Resource Not Found",
                        "You are trying to use a non-existing endpoint"));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleEntityNotFoundException(EntityNotFoundException exception, WebRequest request) {
        logError(exception, request);

        return ResponseEntity
                .status(NOT_FOUND)
                .contentType(APPLICATION_JSON)
                .body(new ErrorDTO(ERROR, exception.getMessage()));
    }

    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorDTO> handleAuthorizationFailedException(AuthorizationFailedException exception, WebRequest request) {
        logWarn(exception, request);

        return ResponseEntity
                .status(UNAUTHORIZED)
                .contentType(APPLICATION_JSON)
                .body(new ErrorDTO(ERROR, exception.getMessage()));
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorDTO> handleSecurityException(SecurityException exception, WebRequest request) {
        logError(exception, request);

        return ResponseEntity
                .status(FORBIDDEN)
                .contentType(APPLICATION_JSON)
                .body(new ErrorDTO(ERROR, exception.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDTO> handleInvalidRequestBody(HttpMessageNotReadableException exception, WebRequest request) {
        logDebug(exception, request);

        return ResponseEntity
                .status(I_AM_A_TEAPOT)
                .contentType(APPLICATION_JSON)
                .body(new ErrorDTO("Invalid Request Body", "Wrong request body used for this endpoint"));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorDTO> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException exception,
            WebRequest request) {
        logError(exception, request);

        return ResponseEntity
                .status(I_AM_A_TEAPOT)
                .contentType(APPLICATION_JSON)
                .body(new ErrorDTO("Invalid Request Method", "Wrong request method for this endpoint"));
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ErrorDTO> handleLockedException(LockedException exception, WebRequest request) {
        logError(exception, request);

        return ResponseEntity
                .status(LOCKED)
                .contentType(APPLICATION_JSON)
                .body(new ErrorDTO(ERROR, exception.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDTO> handleIllegalArgumentException(
            IllegalArgumentException exception, WebRequest request) {
        logError(exception, request);

        return ResponseEntity
                .status(BAD_REQUEST)
                .contentType(APPLICATION_JSON)
                .body(new ErrorDTO(ERROR, exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleAllOtherExceptions(Exception exception, WebRequest request) {
        logError(exception, request);

        return ResponseEntity
                .status(I_AM_A_TEAPOT)
                .contentType(APPLICATION_JSON)
                .body(new ErrorDTO("Houston, we have a problem!",
                        "Something went wrong with your request. Please check and try again."));
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ErrorDTO> handleHttpMediaTypeNotAcceptableException(
            HttpMediaTypeNotAcceptableException exception,
            WebRequest request) {
        logWarn(exception, request);

        return ResponseEntity
                .status(NOT_ACCEPTABLE)
                .contentType(APPLICATION_JSON)
                .body(new ErrorDTO("No Acceptable Representation",
                        "The requested media type is not supported."));
    }

    private void logError(Exception exception, WebRequest request) {
        String logMessage = EXCEPTION_CAUGHT_TEMPLATE.formatted(exception.getMessage(),
                request.getDescription(false));
        log.error(logMessage);
    }

    private void logWarn(Exception exception, WebRequest request) {
        String logMessage = EXCEPTION_CAUGHT_TEMPLATE.formatted(exception.getMessage(),
                request.getDescription(false));
        log.warn(logMessage);
    }

    private void logDebug(Exception exception, WebRequest request) {
        String logMessage = EXCEPTION_CAUGHT_TEMPLATE.formatted(exception.getMessage(),
                request.getDescription(false));
        log.debug(logMessage);
    }
}