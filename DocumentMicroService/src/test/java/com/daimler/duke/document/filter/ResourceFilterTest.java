package com.daimler.duke.document.filter;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;

import com.daimler.duke.document.constant.Constants;
import com.daimler.duke.document.jwt.service.IJwtAuthService;
import com.daimler.duke.document.service.IDocumentAppIdentifierService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

/**
 * unit test for ResourceFilter class.
 * 
 * @author NAYASAR
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({
                  ResourceFilter.class
})
public class ResourceFilterTest {

    private static final String           AUTHENTICATION_SCHEME = "Bearer";

    private static final String           AUTHORIZATION         = "Authorization";

    private static final String           APPLICATION_NAME      = "ApplicationName";

    private static final String           AUTHORIZATION_ERROR   =
            "Authorization header must not be null and must be prefixed with \"Bearer\" plus a whitespace.";

    private ResourceFilter                resourceFilter;
    private HttpServletRequest            request;
    private HttpServletResponse           response;
    private FilterChain                   chain;
    private String                        message;
    private IJwtAuthService               iJwtAuthUtilService;
    private IDocumentAppIdentifierService documentAppIdentifierService;

    @Before
    public void init() throws Exception {
        resourceFilter = new ResourceFilter();
        request = PowerMockito.mock(HttpServletRequest.class);
        response = PowerMockito.mock(HttpServletResponse.class);
        chain = PowerMockito.mock(FilterChain.class);

        iJwtAuthUtilService = PowerMockito.mock(IJwtAuthService.class);
        documentAppIdentifierService = PowerMockito.mock(IDocumentAppIdentifierService.class);
        Whitebox.setInternalState(resourceFilter, "iJwtAuthUtilService", iJwtAuthUtilService);
        Whitebox.setInternalState(resourceFilter, "documentAppIdentifierService", documentAppIdentifierService);
    }

    @Test
    public void testDoFilter() throws IOException, ServletException {
        String uri = "/DocumentMicroService/documentmetadatas/V1/5a3b92963b52d913d4ce3811";
        PowerMockito.when(request.getRequestURI()).thenReturn(uri);

        PowerMockito.when(request.getHeader(AUTHORIZATION)).thenReturn(AUTHENTICATION_SCHEME + " eyJhbGciOiJIUzI1NiJ9");

        PowerMockito.when(request.getHeader(APPLICATION_NAME)).thenReturn("V+jtQF70lKo=12");

        Claims claims = null;
        PowerMockito.when(iJwtAuthUtilService.verify(Mockito.any(String.class))).thenReturn(claims);

        /*
         * PowerMockito.when(documentAppIdentifierService.verifyApplicationName(Mockito.any(Claims.class),
         * Mockito.any(String.class))) .thenReturn(claims);
         */
        // method under test
        resourceFilter.doFilter(request, response, chain);

    }

    @Test
    public void testDoFilterIfInvalidVersionNumber() throws Exception {
        String uri = "/DocumentMicroService/documentmetadatas/V2/5a3b92963b52d913d4ce3811";

        PowerMockito.when(request.getRequestURI()).thenReturn(uri);

        Mockito.doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                // errorCode = (Integer) args[0];
                message = (String) args[1];
                return message;
            }
        }).when(response).sendError(HttpStatus.BAD_REQUEST.value(), Constants.VERSION_NOT_SUPPORTED);

        // method under test
        resourceFilter.doFilter(request, response, chain);
        assertThat("Response payload should be ", message, equalTo(Constants.VERSION_NOT_SUPPORTED));

    }

    @Test
    public void testDoFilterIfApplicationIsNull() throws IOException, ServletException {
        String uri = "/DocumentMicroService/documentmetadatas/V1/5a3b92963b52d913d4ce3811";
        PowerMockito.when(request.getRequestURI()).thenReturn(uri);

        PowerMockito.when(request.getHeader(AUTHORIZATION)).thenReturn(AUTHENTICATION_SCHEME + " eyJhbGciOiJI");

        PowerMockito.when(request.getHeader(APPLICATION_NAME)).thenReturn(null);

        // method under test
        resourceFilter.doFilter(request, response, chain);

    }

    @Test
    public void testDoFilterForInvalidToken() throws IOException, ServletException {
        String uri = "/DocumentMicroService/documentmetadatas/V1/5a3b92963b52d913d4ce3811";
        PowerMockito.when(request.getRequestURI()).thenReturn(uri);

        PowerMockito.when(request.getHeader(AUTHORIZATION)).thenReturn(AUTHENTICATION_SCHEME + " eyJhbGciOiJIUzI1NiJ9");
        PowerMockito.when(request.getHeader(APPLICATION_NAME)).thenReturn("V+jtQF70lKo=12");
        PowerMockito.when(iJwtAuthUtilService.verify(Mockito.any(String.class)))
                    .thenThrow(new JwtException("Invalid Token"));

        /*
         * PowerMockito.when(documentAppIdentifierService.verifyApplicationName(Mockito.any(Claims.class),
         * Mockito.any(String.class))) .thenReturn(claims);
         */
        // method under test
        resourceFilter.doFilter(request, response, chain);
        // assertThat(ex.getMessage(), equalTo("Invalid Token"));
    }

    @Test
    public void testDoFilterForInvalidHeader() throws IOException, ServletException {
        String uri = "/DocumentMicroService/documentContent/V1/content";
        PowerMockito.when(request.getRequestURI()).thenReturn(uri);

        PowerMockito.when(request.getHeader(AUTHORIZATION)).thenReturn(" eyJhbGciOiJIUzI1NiJ9");

        Mockito.doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                // errorCode = (Integer) args[0];
                message = (String) args[1];
                return message;
            }
        }).when(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, AUTHORIZATION_ERROR);

        // method under test
        resourceFilter.doFilter(request, response, chain);
        assertThat("Response payload should be ", message, equalTo(AUTHORIZATION_ERROR));

    }

}
