package com.testmaker.api.exception;

public class DuplicateAnswerException extends RuntimeException {
  public DuplicateAnswerException(String message) {
    super(message);
  }
}
