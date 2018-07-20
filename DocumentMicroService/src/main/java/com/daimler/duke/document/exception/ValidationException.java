package com.daimler.duke.document.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends BaseException {

    /**
     * Teh serial versionUID.
     */
    private static final long serialVersionUID = -1092555783421087108L;

    /**
     * default constructor.
     */

    public ValidationException() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param detail String
     */
    public ValidationException(final String detail) {
        super(detail);
        getErrorContainer().setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        getErrorContainer().setErrorDetails(detail);
    }

    /**
     * @param errorCode.
     * @param detail
     */
    public ValidationException(final int errorCode, final String detail) {
        super(detail);
        getErrorContainer().setErrorCode(errorCode);
        getErrorContainer().setErrorDetails(detail);
    }

    /**
     * @param errorCode.
     * @param detail
     */
    public ValidationException(final int errorCode, final String detail, final String message) {
        super(detail);
        getErrorContainer().setErrorCode(errorCode);
        getErrorContainer().setErrorDetails(detail);
        getErrorContainer().setMessage(message);
    }

    /**
     * @param msg.
     * @param detail
     */
    public ValidationException(final String msg, final String detail) {
        super(msg);
        getErrorContainer().setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        getErrorContainer().setMessage(msg);
        getErrorContainer().setErrorDetails(detail);
    }

}
