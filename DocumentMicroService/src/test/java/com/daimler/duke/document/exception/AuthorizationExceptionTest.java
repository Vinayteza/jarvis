package com.daimler.duke.document.exception;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Test;

public class AuthorizationExceptionTest {

    private AuthorizationException authorizationException;

    @Test
    public void testAuthorizationException() {
        authorizationException = new AuthorizationException();
        Assert.assertNotNull(authorizationException);
        Assert.assertNotNull(authorizationException.getErrorContainer());
        Assert.assertNotNull(authorizationException);
        assertThat(authorizationException.getErrorContainer().getErrorCode(), equalTo(0));
        assertThat(authorizationException.getErrorContainer().getMessage(), equalTo("failed"));
    }

    @Test
    public void testAuthorizationExceptionDetails() {
        authorizationException = new AuthorizationException("Something went wrong");
        Assert.assertNotNull(authorizationException);
        Assert.assertNotNull(authorizationException.getErrorContainer());
        Assert.assertNotNull(authorizationException);
        assertThat(authorizationException.getErrorContainer().getErrorCode(), equalTo(500));
        assertThat(authorizationException.getErrorContainer().getErrorDetails(), equalTo("Something went wrong"));
        assertThat(authorizationException.getErrorContainer().getMessage(), equalTo("failed"));
    }

    @Test
    public void testAuthorizationExceptionDetailsCodeWithMessage() {
        authorizationException = new AuthorizationException(CommonErrorCodes.COMMON001.getErrorCode(),
                                                            CommonErrorCodes.COMMON001.getDescDe(),
                                                            CommonErrorCodes.COMMON001.getDescDe());
        Assert.assertNotNull(authorizationException);
        Assert.assertNotNull(authorizationException.getErrorContainer());
        Assert.assertNotNull(authorizationException);
        assertThat(authorizationException.getErrorContainer().getErrorCode(), equalTo(100));
        assertThat(authorizationException.getErrorContainer().getErrorDetails(), equalTo("Object Not Found"));
        assertThat(authorizationException.getErrorContainer().getMessage(), equalTo("Object Not Found"));
    }

    @Test
    public void testAuthorizationExceptionDetailsWithMessage() {
        authorizationException = new AuthorizationException(CommonErrorCodes.COMMON001.getDescDe(),
                                                            CommonErrorCodes.COMMON001.getDescDe());
        Assert.assertNotNull(authorizationException);
        Assert.assertNotNull(authorizationException.getErrorContainer());
        Assert.assertNotNull(authorizationException);
        assertThat(authorizationException.getErrorContainer().getErrorCode(), equalTo(500));
        assertThat(authorizationException.getErrorContainer().getErrorDetails(), equalTo("Object Not Found"));
        assertThat(authorizationException.getErrorContainer().getMessage(), equalTo("Object Not Found"));
    }

}
