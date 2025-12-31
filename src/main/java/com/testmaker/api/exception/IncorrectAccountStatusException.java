package com.testmaker.api.exception;

public class IncorrectAccountStatusException extends RuntimeException {
    public IncorrectAccountStatusException(String message) {
        super(message);
    }
}
