package com.daimler.duke.document.interceptor;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.daimler.duke.document.dto.RestResponseObject;
import com.daimler.duke.document.exception.AuthorizationException;
import com.daimler.duke.document.exception.CommonErrorCodes;
import com.daimler.duke.document.exception.DocumentException;
import com.daimler.duke.document.exception.EncryptionException;
import com.daimler.duke.document.exception.MongoConnectException;
import com.daimler.duke.document.exception.ValidationException;

public class ExceptionHandlerTest {

    @Before
    public void init() throws Exception {
    }

    @Test
    public void testValidationException() {
        ValidationException exp = new ValidationException(CommonErrorCodes.APPLICATION_NAME_IS_REQUIRED.getErrorCode(),
                                                          CommonErrorCodes.APPLICATION_NAME_IS_REQUIRED.getDescDe());
        ResponseEntity<RestResponseObject> response = ExceptionHandler.handleException(exp);
        Assert.assertNotNull(response);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));

        RestResponseObject body = response.getBody();

        Assert.assertNotNull(body);
        assertThat(body.getStatusMessage(), equalTo("failed"));
        assertThat(body.getStatusMessageDtl(), equalTo(CommonErrorCodes.APPLICATION_NAME_IS_REQUIRED.getDescDe()));
        assertThat(body.getStatusCode(), equalTo(CommonErrorCodes.APPLICATION_NAME_IS_REQUIRED.getErrorCode()));
    }

    @Test
    public void testDocumentException() {
        DocumentException exp = new DocumentException(CommonErrorCodes.ERROR_IN_WRITING_INTO_BYTE_STREAM.getErrorCode(),
                                                      CommonErrorCodes.ERROR_IN_WRITING_INTO_BYTE_STREAM.getDescDe());
        ResponseEntity<RestResponseObject> response = ExceptionHandler.handleException(exp);
        Assert.assertNotNull(response);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));

        RestResponseObject body = response.getBody();

        Assert.assertNotNull(body);
        assertThat(body.getStatusMessage(), equalTo("failed"));
        assertThat(body.getStatusMessageDtl(), equalTo(CommonErrorCodes.ERROR_IN_WRITING_INTO_BYTE_STREAM.getDescDe()));
        assertThat(body.getStatusCode(), equalTo(CommonErrorCodes.ERROR_IN_WRITING_INTO_BYTE_STREAM.getErrorCode()));
    }

    @Test
    public void testEncryptionException() {
        EncryptionException exp = new EncryptionException(CommonErrorCodes.APPLICATION_NAME_IS_REQUIRED.getErrorCode(),
                                                          CommonErrorCodes.APPLICATION_NAME_IS_REQUIRED.getDescDe());
        ResponseEntity<RestResponseObject> response = ExceptionHandler.handleException(exp);
        Assert.assertNotNull(response);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.UNAUTHORIZED));

        RestResponseObject body = response.getBody();

        Assert.assertNotNull(body);
        assertThat(body.getStatusMessage(), equalTo("failed"));
        assertThat(body.getStatusMessageDtl(), equalTo(CommonErrorCodes.APPLICATION_NAME_IS_REQUIRED.getDescDe()));
        assertThat(body.getStatusCode(), equalTo(CommonErrorCodes.APPLICATION_NAME_IS_REQUIRED.getErrorCode()));
    }

    @Test
    public void testAuthorizationException() {
        AuthorizationException exp = new AuthorizationException(CommonErrorCodes.SECRET_NULL_OR_EMPTY.getErrorCode(),
                                                                CommonErrorCodes.SECRET_NULL_OR_EMPTY.getDescDe());
        ResponseEntity<RestResponseObject> response = ExceptionHandler.handleException(exp);
        Assert.assertNotNull(response);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.UNAUTHORIZED));

        RestResponseObject body = response.getBody();

        Assert.assertNotNull(body);
        assertThat(body.getStatusMessage(), equalTo("failed"));
        assertThat(body.getStatusMessageDtl(), equalTo(CommonErrorCodes.SECRET_NULL_OR_EMPTY.getDescDe()));
        assertThat(body.getStatusCode(), equalTo(CommonErrorCodes.SECRET_NULL_OR_EMPTY.getErrorCode()));
    }

    @Test
    public void testRuntimeException() {
        RuntimeException exp = new RuntimeException(MongoConnectException.MONOGO_CONNECT_ERROR_MSG);
        ResponseEntity<RestResponseObject> response = ExceptionHandler.handleException(exp);
        Assert.assertNotNull(response);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.INTERNAL_SERVER_ERROR));

        RestResponseObject body = response.getBody();

        Assert.assertNotNull(body);
        assertThat(body.getStatusMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));
        assertThat(body.getStatusCode(), equalTo(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @Test
    public void testDataAccessResourceFailureException() {
        DataAccessResourceFailureException exp =
                new DataAccessResourceFailureException(MongoConnectException.MONOGO_CONNECT_ERROR_MSG);
        ResponseEntity<RestResponseObject> response = ExceptionHandler.handleException(exp);
        Assert.assertNotNull(response);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.SERVICE_UNAVAILABLE));

        RestResponseObject body = response.getBody();

        Assert.assertNotNull(body);
        assertThat(body.getStatusMessage(), equalTo("failed"));
        assertThat(body.getStatusCode(), equalTo(HttpStatus.SERVICE_UNAVAILABLE.value()));
    }

}
