package com.testmaker.api.exception;

public class DuplicateQuestionException extends RuntimeException {
  public DuplicateQuestionException(String message) {
    super(message);
  }
}
