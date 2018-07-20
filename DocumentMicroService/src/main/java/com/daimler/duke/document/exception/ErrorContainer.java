package com.daimler.duke.document.exception;

import java.io.Serializable;

/**
 * 
 * @author SANDGUP.
 * 
 */

public class ErrorContainer implements Serializable {
  /**
   * The serialVersionUID.
   */
  private static final long  serialVersionUID = 3795515800026895731L;

  /** Constant status failed. */
  public static final String ERROR_MSG        = "failed";

  /** The errorCode. */
  private int                errorCode;

  /** The errorDetails. */
  private String             errorDetails;

  /** The errorMessage. */
  private String             message          = ERROR_MSG;

  /**
   * 
   * @return
   */
  public int getErrorCode() {
    return errorCode;
  }

  /**
   * 
   * @return
   */
  public String getErrorDetails() {
    return errorDetails;
  }

  /**
   * 
   * @return
   */
  public String getMessage() {
    return message;
  }

  /**
   * 
   * @param errorCode
   */
  public void setErrorCode(final int errorCode) {
    this.errorCode = errorCode;
  }

  /**
   * 
   * @param errorDetails
   */
  public void setErrorDetails(final String errorDetails) {
    this.errorDetails = errorDetails;
  }

  /**
   * 
   * @param msg
   */
  public void setMessage(final String msg) {
    this.message = msg;
  }

}
