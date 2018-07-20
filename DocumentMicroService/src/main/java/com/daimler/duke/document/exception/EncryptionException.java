package com.daimler.duke.document.exception;

import org.springframework.http.HttpStatus;

/**
 * Encryption service fault.
 *
 * @author rmahaku
 *
 */
public class EncryptionException extends BaseException {

    /**
     * default serial versionID.
     */
    private static final long serialVersionUID = 4247641885757830650L;

    /**
     * default constructor.
     */

    public EncryptionException() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param detail String
     */
    public EncryptionException(final String detail) {
        super(detail);
        getErrorContainer().setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        getErrorContainer().setErrorDetails(detail);
    }

    /**
     * @param errorCode.
     * @param detail
     */
    public EncryptionException(final int errorCode, final String detail) {
        super(detail);
        getErrorContainer().setErrorCode(errorCode);
        getErrorContainer().setErrorDetails(detail);
    }

    /**
     * @param errorCode.
     * @param detail
     */
    public EncryptionException(final int errorCode, final String detail, final String message) {
        super(detail);
        getErrorContainer().setErrorCode(errorCode);
        getErrorContainer().setErrorDetails(detail);
        getErrorContainer().setMessage(message);
    }

    /**
     * @param msg.
     * @param detail
     */
    public EncryptionException(final String msg, final String detail) {
        super(msg);
        getErrorContainer().setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        getErrorContainer().setMessage(msg);
        getErrorContainer().setErrorDetails(detail);
    }

}
