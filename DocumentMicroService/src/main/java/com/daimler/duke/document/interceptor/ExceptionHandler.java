package com.daimler.duke.document.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.daimler.duke.document.dto.RestResponseObject;
import com.daimler.duke.document.exception.AuthorizationException;
import com.daimler.duke.document.exception.BaseException;
import com.daimler.duke.document.exception.DocumentException;
import com.daimler.duke.document.exception.EncryptionException;
import com.daimler.duke.document.exception.MongoConnectException;
import com.daimler.duke.document.exception.ValidationException;

/**
 * 
 * @author SANDGUP.
 *
 */
public final class ExceptionHandler {
    /**
     * logger instance
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);

    /**
     * private contructor to prevent the instantiation.
     */
    private ExceptionHandler() {
    }

    /**
     * This method is responsible to check the catched exception.
     * 
     * @return the RestResponseObject
     */
    public static ResponseEntity<RestResponseObject> handleException(final Exception exp) {
        final RestResponseObject response = new RestResponseObject();
        if (exp instanceof BaseException) {
            final BaseException baseException = (BaseException) exp;
            response.setStatusCode(baseException.getErrorContainer().getErrorCode());
            response.setStatusMessage(baseException.getErrorContainer().getMessage());
            response.setStatusMessageDtl(baseException.getErrorContainer().getErrorDetails());
        }
        else {
            LOGGER.error(exp.getMessage(), exp);
            // System.out.println(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setStatusMessage(exp.getMessage());
        }

        if (exp instanceof ValidationException) {
            return new ResponseEntity<RestResponseObject>(response, HttpStatus.BAD_REQUEST);
        }
        else if (exp instanceof DocumentException) {
            return new ResponseEntity<RestResponseObject>(response, HttpStatus.BAD_REQUEST);
        }
        else if (exp instanceof EncryptionException) {
            return new ResponseEntity<RestResponseObject>(response, HttpStatus.UNAUTHORIZED);
        }
        else if (exp instanceof AuthorizationException) {
            return new ResponseEntity<RestResponseObject>(response, HttpStatus.UNAUTHORIZED);
        }
        else if (exp instanceof DataAccessResourceFailureException) {
            response.setStatusMessage("failed");
            response.setStatusCode(HttpStatus.SERVICE_UNAVAILABLE.value());
            response.setStatusMessageDtl(MongoConnectException.MONOGO_CONNECT_ERROR_MSG);
            return new ResponseEntity<RestResponseObject>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }

        return new ResponseEntity<RestResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
