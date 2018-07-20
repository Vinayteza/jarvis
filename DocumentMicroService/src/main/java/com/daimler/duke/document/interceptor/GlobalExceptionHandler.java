package com.daimler.duke.document.interceptor;

import java.io.Serializable;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 
 * @author SANDGUP.
 *
 */
@RestControllerAdvice
public class GlobalExceptionHandler implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8231015259722298368L;

  /**
   * Default constructor.
   */
  public GlobalExceptionHandler() {

  }

  /**
   * @param exp
   * @return
   */
  @ExceptionHandler(Exception.class)
  public Object excuteCallAndCheckException(final Exception exp) {
    return com.daimler.duke.document.interceptor.ExceptionHandler.handleException(exp);
  }

}
