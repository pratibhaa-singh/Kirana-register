package com.kiranaregister.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApplicationException extends Exception {
  private String message;

  private HttpStatus httpStatusCode;

  public ApplicationException(HttpStatus httpStatusCode, String message) {
    this.httpStatusCode = httpStatusCode;
    this.message = message;
  }
}
