package com.daimler.duke.document.exception;

import org.springframework.http.HttpStatus;

public class AuthorizationException extends BaseException {

    private static final long serialVersionUID = 2781701170054991003L;

    /**
     * default constructor.
     */

    public AuthorizationException() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param detail String
     */
    public AuthorizationException(final String detail) {
        super(detail);
        getErrorContainer().setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        getErrorContainer().setErrorDetails(detail);
    }

    /**
     * @param errorCode.
     * @param detail
     */
    public AuthorizationException(final int errorCode, final String detail) {
        super(detail);
        getErrorContainer().setErrorCode(errorCode);
        getErrorContainer().setErrorDetails(detail);
    }

    /**
     * @param errorCode.
     * @param detail
     */
    public AuthorizationException(final int errorCode, final String detail, final String message) {
        super(detail);
        getErrorContainer().setErrorCode(errorCode);
        getErrorContainer().setErrorDetails(detail);
        getErrorContainer().setMessage(message);
    }

    /**
     * @param msg.
     * @param detail
     */
    public AuthorizationException(final String msg, final String detail) {
        super(msg);
        getErrorContainer().setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        getErrorContainer().setMessage(msg);
        getErrorContainer().setErrorDetails(detail);
    }

}
