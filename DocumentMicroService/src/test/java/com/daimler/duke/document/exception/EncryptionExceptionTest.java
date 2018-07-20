package com.daimler.duke.document.exception;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Test;

public class EncryptionExceptionTest {

    private EncryptionException encryptionException;

    @Test
    public void testAuthorizationException() {
        encryptionException = new EncryptionException();
        Assert.assertNotNull(encryptionException);
        Assert.assertNotNull(encryptionException.getErrorContainer());
        Assert.assertNotNull(encryptionException);
        assertThat(encryptionException.getErrorContainer().getErrorCode(), equalTo(0));
        assertThat(encryptionException.getErrorContainer().getMessage(), equalTo("failed"));
    }

    @Test
    public void testAuthorizationExceptionDetails() {
        encryptionException = new EncryptionException("Something went wrong");
        Assert.assertNotNull(encryptionException);
        Assert.assertNotNull(encryptionException.getErrorContainer());
        Assert.assertNotNull(encryptionException);
        assertThat(encryptionException.getErrorContainer().getErrorCode(), equalTo(500));
        assertThat(encryptionException.getErrorContainer().getErrorDetails(), equalTo("Something went wrong"));
        assertThat(encryptionException.getErrorContainer().getMessage(), equalTo("failed"));
    }

    @Test
    public void testAuthorizationExceptionDetailsCodeWithMessage() {
        encryptionException = new EncryptionException(CommonErrorCodes.COMMON001.getErrorCode(),
                                                      CommonErrorCodes.COMMON001.getDescDe(),
                                                      CommonErrorCodes.COMMON001.getDescDe());
        Assert.assertNotNull(encryptionException);
        Assert.assertNotNull(encryptionException.getErrorContainer());
        Assert.assertNotNull(encryptionException);
        assertThat(encryptionException.getErrorContainer().getErrorCode(), equalTo(100));
        assertThat(encryptionException.getErrorContainer().getErrorDetails(), equalTo("Object Not Found"));
        assertThat(encryptionException.getErrorContainer().getMessage(), equalTo("Object Not Found"));
    }

    @Test
    public void testAuthorizationExceptionDetailsWithMessage() {
        encryptionException =
                new EncryptionException(CommonErrorCodes.COMMON001.getDescDe(), CommonErrorCodes.COMMON001.getDescDe());
        Assert.assertNotNull(encryptionException);
        Assert.assertNotNull(encryptionException.getErrorContainer());
        Assert.assertNotNull(encryptionException);
        assertThat(encryptionException.getErrorContainer().getErrorCode(), equalTo(500));
        assertThat(encryptionException.getErrorContainer().getErrorDetails(), equalTo("Object Not Found"));
        assertThat(encryptionException.getErrorContainer().getMessage(), equalTo("Object Not Found"));
    }

}
