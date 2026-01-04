package com.testmaker.api.exception;

import com.testmaker.api.dto.exception.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final HttpServletRequest request;

    @ExceptionHandler({
            IncorrectCodeException.class,
            EmailNotVerifiedException.class,
            PasswordsDoNotMatchException.class,
            StatusNotFoundException.class,
            IncorrectAccountStatusException.class,
            ExpiredCodeException.class,
            InvalidCodeTypeException.class,
            InvalidPasswordException.class
    })
    public ResponseEntity<ExceptionResponse> handleCustomExceptions(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler({ NoResourceFoundException.class })
    public ResponseEntity<ExceptionResponse> handleNotFoundExceptions() {
        String message = request.getMethod() + " " + request.getRequestURI() + " not found";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(message));
    }

    @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
    public ResponseEntity<ExceptionResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        String message =  e.getMethod() + " http method not found for " + request.getRequestURI();
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ExceptionResponse(message));
    }

    @ExceptionHandler({ HttpMessageNotReadableException.class })
    public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadableException() {
        String message = "Request body is not readable! Please check your body and try again";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(message));
    }

    @ExceptionHandler({ HttpMediaTypeNotSupportedException.class })
    public ResponseEntity<ExceptionResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ObjectError error = e.getBindingResult().getAllErrors().get(0); // Get the first error that caused this exception
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(error.getDefaultMessage()));
    }

    @ExceptionHandler({ MissingServletRequestParameterException.class })
    public ResponseEntity<ExceptionResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        String message = "Missing request parameter '" + e.getParameterName() + "'! Please check your parameters and try again";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(message));
    }

    @ExceptionHandler({ InsufficientAuthenticationException.class })
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionResponse("You are not logged in! Please login to continue"));
    }

    @ExceptionHandler({ UsernameNotFoundException.class, BadCredentialsException.class })
    public ResponseEntity<ExceptionResponse> handleAuthenticationExceptions(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler({ DuplicateKeyException.class })
    public ResponseEntity<ExceptionResponse> handleDataIntegrityViolationExceptions(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(e.getMessage()));
    }

    // Here we catch a constraint exception thrown while executing an SQL statement. We should not provide the user with the error message
    // There is no feasible way to determine which constraint was violated so we provide a generic message. The service layer should make it hard for this exception to be thrown
    @ExceptionHandler({ SQLIntegrityConstraintViolationException.class })
    public ResponseEntity<ExceptionResponse> handleSQLIntegrityConstraintViolationException() {
        String message = "Your request violates the integrity of our application";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(message));
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<ExceptionResponse> handleUncaughtExceptions(Exception e) {
        System.out.println(e);
        e.printStackTrace();
        String message = "Something went very wrong! Please try again later";
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(message));
    }
}
