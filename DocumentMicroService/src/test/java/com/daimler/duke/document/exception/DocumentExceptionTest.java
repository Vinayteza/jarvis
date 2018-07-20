package com.daimler.duke.document.exception;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Test;

public class DocumentExceptionTest {

    private DocumentException documentException;

    @Test
    public void testDocumentException() {
        documentException = new DocumentException();
        Assert.assertNotNull(documentException);
        Assert.assertNotNull(documentException.getErrorContainer());
        Assert.assertNotNull(documentException);
        assertThat(documentException.getErrorContainer().getErrorCode(), equalTo(0));
        assertThat(documentException.getErrorContainer().getMessage(), equalTo("failed"));
    }

    @Test
    public void testDocumentExceptionDetails() {
        documentException = new DocumentException("Something went wrong");
        Assert.assertNotNull(documentException);
        Assert.assertNotNull(documentException.getErrorContainer());
        Assert.assertNotNull(documentException);
        assertThat(documentException.getErrorContainer().getErrorCode(), equalTo(500));
        assertThat(documentException.getErrorContainer().getErrorDetails(), equalTo("Something went wrong"));
        assertThat(documentException.getErrorContainer().getMessage(), equalTo("failed"));
    }

    @Test
    public void testDocumentExceptionDetailsCodeWithMessage() {
        documentException = new DocumentException(CommonErrorCodes.COMMON001.getErrorCode(),
                                                  CommonErrorCodes.COMMON001.getDescDe(),
                                                  CommonErrorCodes.COMMON001.getDescDe());
        Assert.assertNotNull(documentException);
        Assert.assertNotNull(documentException.getErrorContainer());
        Assert.assertNotNull(documentException);
        assertThat(documentException.getErrorContainer().getErrorCode(), equalTo(100));
        assertThat(documentException.getErrorContainer().getErrorDetails(), equalTo("Object Not Found"));
        assertThat(documentException.getErrorContainer().getMessage(), equalTo("Object Not Found"));
    }

    @Test
    public void testDocumentExceptionDetailsWithMessage() {
        documentException =
                new DocumentException(CommonErrorCodes.COMMON001.getDescDe(), CommonErrorCodes.COMMON001.getDescDe());
        Assert.assertNotNull(documentException);
        Assert.assertNotNull(documentException.getErrorContainer());
        Assert.assertNotNull(documentException);
        assertThat(documentException.getErrorContainer().getErrorCode(), equalTo(500));
        assertThat(documentException.getErrorContainer().getErrorDetails(), equalTo("Object Not Found"));
        assertThat(documentException.getErrorContainer().getMessage(), equalTo("Object Not Found"));
    }

}
