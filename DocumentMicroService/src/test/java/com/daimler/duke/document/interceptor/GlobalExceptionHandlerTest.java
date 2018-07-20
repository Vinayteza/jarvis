package com.daimler.duke.document.interceptor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.daimler.duke.document.exception.CommonErrorCodes;
import com.daimler.duke.document.exception.ValidationException;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @Before
    public void init() throws Exception {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    public void testValidationException() {
        ValidationException exp = new ValidationException(CommonErrorCodes.APPLICATION_NAME_IS_REQUIRED.getErrorCode(),
                                                          CommonErrorCodes.APPLICATION_NAME_IS_REQUIRED.getDescDe());
        Object expObj = globalExceptionHandler.excuteCallAndCheckException(exp);
        Assert.assertNotNull(expObj);

    }

}
