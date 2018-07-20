package com.daimler.duke.document.filter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
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
import com.daimler.duke.document.db.entity.DbTokenStatus;
import com.daimler.duke.document.exception.CommonErrorCodes;
import com.daimler.duke.document.jwt.service.IJwtAuthService;
import com.daimler.duke.document.service.TokenStatusService;

import io.jsonwebtoken.JwtException;

@WebFilter(urlPatterns = {
                           "/DocumentMicroService/documentContent/*", "/DocumentMicroService/chunk/*",
})
public class ResourceTokenFilter extends BaseFilter implements Filter {

    private static final Logger LOGGER            = LoggerFactory.getLogger(ResourceTokenFilter.class);

    private List<String>        supportedVersions = Arrays.asList(Constants.V1);

    @Autowired
    private IJwtAuthService     iJwtAuthUtilService;

    @Inject
    private TokenStatusService  tokenStatusService;

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

        LOGGER.info("Authorization: " + authHeader);

        // Validate the Authorization header
        if (!isValidJWT(authHeader)) {
            abortWithUnauthorized(httpResp);
            return;
        }

        // Extract the token from the Authorization header
        String token = authHeader.substring(Constants.AUTHENTICATION_SCHEME.length()).trim();

        // Verify the JWT token for 3rd party client Request.

        if (!StringUtils.isEmpty(token)) {
            DbTokenStatus tokenObj = null;
            try {
                tokenObj = tokenStatusService.findTokenStatusByToken(token);

                /**
                 * Validate the expire date for that token
                 */
                if (!validateExpireDate(tokenObj)) {
                    httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED, CommonErrorCodes.TOKEN_EXPIRED.getDescDe());
                    return;
                }

                /**
                 * Validate the token is active
                 */
                if (tokenObj != null && !StringUtils.isEmpty(tokenObj.getTokenValue())
                        && !StringUtils.isEmpty(tokenObj.getActive())
                        && "T".equals(tokenObj.getActive())) {
                    token = tokenObj.getTokenValue();
                }
                else {
                    httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED, CommonErrorCodes.TOKEN_USED.getDescDe());
                    return;
                }
            }
            catch (Exception e) {
                httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED, CommonErrorCodes.INVALID_TOKEN.getDescDe());
                return;
            }
        }

        try {
            iJwtAuthUtilService.verify(token);
        }
        catch (JwtException e) {
            httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED, CommonErrorCodes.INVALID_TOKEN.getDescDe());
            return;
        }

        chain.doFilter(request, response);

    }

    private boolean validateExpireDate(DbTokenStatus tokenObj) throws IOException {

        if (tokenObj != null && tokenObj.getCreateTime() != null) {
            Date createdDate = tokenObj.getCreateTime();
            LocalDateTime createdlocalDate = createdDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            LOGGER.info("Token Created Date: " + createdlocalDate.toString());
            LOGGER.info("Token Expired Date: " + createdlocalDate.plusDays(Constants.EXPIRE_DATE).toString());

            if (createdlocalDate.plusDays(Constants.EXPIRE_DATE).compareTo(LocalDateTime.now()) >= 0) {
                LOGGER.info("Token Expired Valid");
                return true;
            }
            else {
                LOGGER.info("Token has been expired");
                return false;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

}
