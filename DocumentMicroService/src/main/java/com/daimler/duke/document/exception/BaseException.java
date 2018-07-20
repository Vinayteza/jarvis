package com.daimler.duke.document.exception;

import java.io.Serializable;

/**
 * 
 * @author RMAHAKU
 *
 */
public class BaseException extends RuntimeException implements Serializable {
  /**
   * The serial version UID.
   */
  private static final long    serialVersionUID = -6294167946032724775L;
  /**
   * A object to hold all the necessary information of the exception.
   */

  private final ErrorContainer errorContainer   = new ErrorContainer();

  /**
   * default constructor.
   */
  public BaseException() {
    super();
  }

  /**
   * constructor.
   * 
   * @param msg String
   */
  public BaseException(final String msg) {
    super(msg);
  }

  /**
   * 
   * @return
   */
  public ErrorContainer getErrorContainer() {
    return errorContainer;
  }

}
