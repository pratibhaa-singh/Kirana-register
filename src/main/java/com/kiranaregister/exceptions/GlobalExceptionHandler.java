package com.kiranaregister.exceptions;

import com.kiranaregister.dtos.Meta;
import com.kiranaregister.dtos.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler({ApplicationException.class})
  public ResponseEntity<ResponseDto> handleApplicationException(
      ApplicationException applicationException) {
    logger.error(applicationException.getMessage());
    ResponseDto responseDto = new ResponseDto();
    responseDto.setMeta(new Meta(applicationException.getMessage()));
    return new ResponseEntity(responseDto, applicationException.getHttpStatusCode());
  }

  @ExceptionHandler({RuntimeException.class})
  public ResponseEntity<ResponseDto> handleRuntimeException(RuntimeException runtimeException) {
    logger.error(runtimeException.getMessage());
    ResponseDto responseDto = new ResponseDto();
    responseDto.setMeta(new Meta(runtimeException.getMessage()));
    return new ResponseEntity(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<ResponseDto> handleMethodArgumentException(
      MethodArgumentNotValidException methodArgumentNotValidException) {
    logger.error(methodArgumentNotValidException.getMessage());
    ResponseDto responseDto = new ResponseDto();
    responseDto.setMeta(new Meta(methodArgumentNotValidException.getMessage()));
    return new ResponseEntity(responseDto, HttpStatus.BAD_REQUEST);
  }
}
