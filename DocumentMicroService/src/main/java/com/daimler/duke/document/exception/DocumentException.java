package com.daimler.duke.document.exception;

import org.springframework.http.HttpStatus;

/**
 * MonngoConnectException.
 * 
 * @author SANDGUP.
 *
 */

public class DocumentException extends BaseException {

    /**
     * Teh serial versionUID.
     */
    private static final long serialVersionUID = -1092555783421087108L;

    /**
     * default constructor.
     */

    public DocumentException() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param detail String
     */
    public DocumentException(final String detail) {
        super(detail);
        getErrorContainer().setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        getErrorContainer().setErrorDetails(detail);
    }

    /**
     * @param errorCode.
     * @param detail
     */
    public DocumentException(final int errorCode, final String detail) {
        super(detail);
        getErrorContainer().setErrorCode(errorCode);
        getErrorContainer().setErrorDetails(detail);
    }

    /**
     * @param errorCode.
     * @param detail
     */
    public DocumentException(final int errorCode, final String detail, final String message) {
        super(detail);
        getErrorContainer().setErrorCode(errorCode);
        getErrorContainer().setErrorDetails(detail);
        getErrorContainer().setMessage(message);
    }

    /**
     * @param msg.
     * @param detail
     */
    public DocumentException(final String msg, final String detail) {
        super(msg);
        getErrorContainer().setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        getErrorContainer().setMessage(msg);
        getErrorContainer().setErrorDetails(detail);
    }

}
