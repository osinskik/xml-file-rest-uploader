package com.osinskik.demo.xmlfilerestuploader.web.controller;

import javax.validation.ConstraintViolationException;

import com.osinskik.demo.xmlfilerestuploader.web.dto.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerErrorAdvice {

  private static final Logger LOGGER = LoggerFactory.getLogger(ControllerErrorAdvice.class);

  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ IllegalArgumentException.class })
  public ErrorMessage handleIllegalArgumentException(IllegalArgumentException e) {
    LOGGER.error("Illegal Argument Exception", e);
    return new ErrorMessage(e.getMessage());
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ConstraintViolationException.class)
  public ErrorMessage handleConstraintViolationException(ConstraintViolationException e) {
    LOGGER.error("Constraint violation exception", e);
    return new ErrorMessage(e.getMessage());
  }

}
