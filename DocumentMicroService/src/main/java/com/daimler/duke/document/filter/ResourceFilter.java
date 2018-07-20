package com.daimler.duke.document.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.daimler.duke.document.constant.Constants;
import com.daimler.duke.document.exception.CommonErrorCodes;
import com.daimler.duke.document.jwt.service.IJwtAuthService;
import com.daimler.duke.document.service.IDocumentAppIdentifierService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

/**
 * Resource filter, to Version number check in URI. Example for RestClient URL:
 * http://localhost:8080/DocumentMicroService/documentmetadatas/V1/5a3b92963b52d913d4ce3811
 * 
 * @author NAYASAR
 *
 */
@WebFilter(urlPatterns = {
                           "/DocumentMicroService/documentmetadatas/*", "/DocumentMicroService/documents/*"
})
public class ResourceFilter extends BaseFilter implements Filter {

    private static final Logger           LOGGER            = LoggerFactory.getLogger(ResourceFilter.class);

    private List<String>                  supportedVersions = Arrays.asList(Constants.V1);

    /**
     * jwt service.
     */
    @Autowired
    private IJwtAuthService               iJwtAuthUtilService;

    @Autowired
    private IDocumentAppIdentifierService documentAppIdentifierService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        final HttpServletRequest httpReq = (HttpServletRequest) request;
        final HttpServletResponse httpResp = (HttpServletResponse) response;
        String uri = httpReq.getRequestURI();

        /**
         * If version is not supported or request without a version Ex: URI :
         * /DocumentMicroService/documentmetadatas/V1/5a3b92963b52d913d4ce3811
         */
        if (!checkSupportedVersions(uri, supportedVersions)) {
            httpResp.sendError(HttpStatus.BAD_REQUEST.value(), Constants.VERSION_NOT_SUPPORTED);
            return;
        }

        // JWT security check here.
        final String authHeader = httpReq.getHeader(Constants.AUTHORIZATION);

        String applicationName = httpReq.getHeader(Constants.APPLICATION_NAME);

        LOGGER.info("Authorization: " + authHeader + "Encrypted Application Name: " + applicationName);

        // Validate the Authorization header
        if (!isValidJWT(authHeader)) {
            abortWithUnauthorized(httpResp);
            return;
        }

        if (StringUtils.isEmpty(applicationName)) {
            httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                               CommonErrorCodes.APPLICATION_NAME_IS_REQUIRED.getDescDe());
            return;
        }

        // Extract the token from the Authorization header
        String token = authHeader.substring(Constants.AUTHENTICATION_SCHEME.length()).trim();

        // Verify the JWT token here.

        Claims claims = null;
        try {
            claims = iJwtAuthUtilService.verify(token);
        }
        catch (JwtException e) {
            httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED, CommonErrorCodes.INVALID_TOKEN.getDescDe());
            return;
        }

        // Verify the applicationName here.

        try {
            documentAppIdentifierService.verifyApplicationName(claims, applicationName);
        }
        catch (Exception de) {
            httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED, de.getMessage());
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }
}
