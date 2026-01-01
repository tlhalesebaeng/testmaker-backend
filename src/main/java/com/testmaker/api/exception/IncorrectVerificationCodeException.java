package com.testmaker.api.exception;

public class IncorrectVerificationCodeException extends RuntimeException {
    public IncorrectVerificationCodeException(String message) {
        super(message);
    }
}
