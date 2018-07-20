package com.daimler.duke.document.filter;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Date;

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
import com.daimler.duke.document.db.entity.DbTokenStatus;
import com.daimler.duke.document.jwt.service.IJwtAuthService;
import com.daimler.duke.document.service.TokenStatusService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
                  ResourceTokenFilter.class
})
public class ResourceTokenFilterTest {

    private static final String AUTHENTICATION_SCHEME = "Bearer";

    private static final String AUTHORIZATION         = "Authorization";
    private static final String AUTHORIZATION_ERROR   =
            "Authorization header must not be null and must be prefixed with \"Bearer\" plus a whitespace.";

    private ResourceTokenFilter resourceTokenFilter;
    private HttpServletRequest  request;
    private HttpServletResponse response;
    private FilterChain         chain;
    private String              message;
    private IJwtAuthService     iJwtAuthUtilService;
    private TokenStatusService  tokenStatusService;

    @Before
    public void init() throws Exception {
        resourceTokenFilter = new ResourceTokenFilter();
        request = PowerMockito.mock(HttpServletRequest.class);
        response = PowerMockito.mock(HttpServletResponse.class);
        chain = PowerMockito.mock(FilterChain.class);

        iJwtAuthUtilService = PowerMockito.mock(IJwtAuthService.class);
        tokenStatusService = PowerMockito.mock(TokenStatusService.class);

        Whitebox.setInternalState(resourceTokenFilter, "iJwtAuthUtilService", iJwtAuthUtilService);
        Whitebox.setInternalState(resourceTokenFilter, "tokenStatusService", tokenStatusService);
    }

    @Test
    public void testDoFilter() throws IOException, ServletException {
        String uri = "/DocumentMicroService/documentContent/V1/content";
        PowerMockito.when(request.getRequestURI()).thenReturn(uri);

        PowerMockito.when(request.getHeader(AUTHORIZATION)).thenReturn(AUTHENTICATION_SCHEME + " eyJhbGciOiJIUzI1NiJ9");

        DbTokenStatus dbTokenStatus = new DbTokenStatus();
        dbTokenStatus.setActive("T");
        dbTokenStatus.setDocumentId("6456456457");
        dbTokenStatus.setTokenValue("56457yrtytrutru");
        dbTokenStatus.setCreateTime(new Date());
        PowerMockito.when(tokenStatusService.findTokenStatusByToken(Mockito.any(String.class)))
                    .thenReturn(dbTokenStatus);

        Claims claims = null;
        PowerMockito.when(iJwtAuthUtilService.verify(Mockito.any(String.class))).thenReturn(claims);

        /*
         * PowerMockito.when(documentAppIdentifierService.verifyApplicationName(Mockito.any(Claims.class),
         * Mockito.any(String.class))) .thenReturn(claims);
         */
        // method under test
        resourceTokenFilter.doFilter(request, response, chain);

    }

    @Test
    public void testDoFilterIfInvalidVersionNumber() throws Exception {
        String uri = "/DocumentMicroService/documentContent/V2/content";

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
        resourceTokenFilter.doFilter(request, response, chain);
        assertThat("Response payload should be ", message, equalTo(Constants.VERSION_NOT_SUPPORTED));

    }

    @Test
    public void testDoFilterIfApplicationIsNull() throws IOException, ServletException {
        String uri = "/DocumentMicroService/documentContent/V1/content";
        PowerMockito.when(request.getRequestURI()).thenReturn(uri);

        PowerMockito.when(request.getHeader(AUTHORIZATION)).thenReturn(AUTHENTICATION_SCHEME + " eyJhbGciOiJI");

        // method under test
        resourceTokenFilter.doFilter(request, response, chain);

    }

    @Test
    public void testDoFilterForInvalidToken() throws IOException, ServletException {
        try {
            String uri = "/DocumentMicroService/documentContent/V1/content";
            PowerMockito.when(request.getRequestURI()).thenReturn(uri);

            PowerMockito.when(request.getHeader(AUTHORIZATION))
                        .thenReturn(AUTHENTICATION_SCHEME + " eyJhbGciOiJIUzI1NiJ9");

            DbTokenStatus dbTokenStatus = new DbTokenStatus();
            dbTokenStatus.setActive("T");
            dbTokenStatus.setDocumentId("6456456457");
            dbTokenStatus.setTokenValue("56457yrtytrutru");
            dbTokenStatus.setCreateTime(new Date());
            PowerMockito.when(tokenStatusService.findTokenStatusByToken(Mockito.any(String.class)))
                        .thenReturn(dbTokenStatus);

            PowerMockito.when(iJwtAuthUtilService.verify(Mockito.any(String.class)))
                        .thenThrow(new JwtException("Invalid Token"));

            /*
             * PowerMockito.when(documentAppIdentifierService.verifyApplicationName(Mockito.any(Claims.class),
             * Mockito.any(String.class))) .thenReturn(claims);
             */
            // method under test
            resourceTokenFilter.doFilter(request, response, chain);
        }
        catch (Exception ex) {
            assertThat(ex.getMessage(), equalTo("Invalid Token"));

        }

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
        resourceTokenFilter.doFilter(request, response, chain);
        assertThat("Response payload should be ", message, equalTo(AUTHORIZATION_ERROR));

    }

}
