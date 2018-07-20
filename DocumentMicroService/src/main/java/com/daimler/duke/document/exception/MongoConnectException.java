package com.daimler.duke.document.exception;

/**
 * MonngoConnectException.
 * 
 * @author SANDGUP.
 *
 */

public class MongoConnectException extends BaseException {
  /**
   * 
   */
  public static final int    MONOGO_CONNECT_ERROR_CODE = 800;
  /**
   * 
   */
  public static final String MONOGO_CONNECT_ERROR_MSG  = "error in getting mongo connection";

  /**
   * Teh serial versionUID.
   */
  private static final long  serialVersionUID          = -1092555783421087108L;

  /**
   * default constructor.
   */

  public MongoConnectException() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param detail String
   */
  public MongoConnectException(final String detail) {
    super(detail);
    getErrorContainer().setErrorCode(MONOGO_CONNECT_ERROR_CODE);
    getErrorContainer().setErrorDetails(detail);
  }

  /**
   * @param msg.
   * @param detail
   */
  public MongoConnectException(final String msg, final String detail) {
    super(msg);
    getErrorContainer().setErrorCode(MONOGO_CONNECT_ERROR_CODE);
    getErrorContainer().setMessage(msg);
    getErrorContainer().setErrorDetails(detail);
  }

}
