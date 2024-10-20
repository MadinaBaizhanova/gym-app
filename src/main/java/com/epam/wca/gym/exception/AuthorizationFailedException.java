package com.epam.wca.gym.exception;

public class AuthorizationFailedException extends RuntimeException {

    public AuthorizationFailedException(String message) {
        super(message);
    }
}