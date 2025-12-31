package com.testmaker.api.exception;

import com.testmaker.api.dto.exception.ExceptionResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({
            InvalidVerificationCodeException.class,
            EmailNotVerifiedException.class,
            PasswordsDoNotMatchException.class,
            StatusNotFoundException.class
    })
    public ResponseEntity<ExceptionResponse> handleCustomExceptions(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ObjectError error = e.getBindingResult().getAllErrors().get(0); // Get the first error that caused this exception
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(error.getDefaultMessage()));
    }

    @ExceptionHandler({ UsernameNotFoundException.class, BadCredentialsException.class })
    public ResponseEntity<ExceptionResponse> handleAuthenticationExceptions(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler({ DuplicateKeyException.class })
    public ResponseEntity<ExceptionResponse> handleDataIntegrityViolationExceptions(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(e.getMessage()));
    }
}
