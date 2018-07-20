package com.daimler.duke.document.exception;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Test;

public class ValidationExceptionTest {

    private ValidationException validationException;

    @Test
    public void testValidationException() {
        validationException = new ValidationException();
        Assert.assertNotNull(validationException);
        Assert.assertNotNull(validationException.getErrorContainer());
        Assert.assertNotNull(validationException);
        assertThat(validationException.getErrorContainer().getErrorCode(), equalTo(0));
        assertThat(validationException.getErrorContainer().getMessage(), equalTo("failed"));
    }

    @Test
    public void testValidationExceptionDetails() {
        validationException = new ValidationException("Something went wrong");
        Assert.assertNotNull(validationException);
        Assert.assertNotNull(validationException.getErrorContainer());
        Assert.assertNotNull(validationException);
        assertThat(validationException.getErrorContainer().getErrorCode(), equalTo(500));
        assertThat(validationException.getErrorContainer().getErrorDetails(), equalTo("Something went wrong"));
        assertThat(validationException.getErrorContainer().getMessage(), equalTo("failed"));
    }

    @Test
    public void testValidationExceptionDetailsCodeWithMessage() {
        validationException = new ValidationException(CommonErrorCodes.COMMON001.getErrorCode(),
                                                      CommonErrorCodes.COMMON001.getDescDe(),
                                                      CommonErrorCodes.COMMON001.getDescDe());
        Assert.assertNotNull(validationException);
        Assert.assertNotNull(validationException.getErrorContainer());
        Assert.assertNotNull(validationException);
        assertThat(validationException.getErrorContainer().getErrorCode(), equalTo(100));
        assertThat(validationException.getErrorContainer().getErrorDetails(), equalTo("Object Not Found"));
        assertThat(validationException.getErrorContainer().getMessage(), equalTo("Object Not Found"));
    }

    @Test
    public void testValidationExceptionDetailsWithMessage() {
        validationException =
                new ValidationException(CommonErrorCodes.COMMON001.getDescDe(), CommonErrorCodes.COMMON001.getDescDe());
        Assert.assertNotNull(validationException);
        Assert.assertNotNull(validationException.getErrorContainer());
        Assert.assertNotNull(validationException);
        assertThat(validationException.getErrorContainer().getErrorCode(), equalTo(500));
        assertThat(validationException.getErrorContainer().getErrorDetails(), equalTo("Object Not Found"));
        assertThat(validationException.getErrorContainer().getMessage(), equalTo("Object Not Found"));
    }

}
