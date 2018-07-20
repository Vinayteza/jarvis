package com.daimler.duke.document.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.daimler.duke.document.constant.Constants;

public abstract class BaseFilter {

    protected boolean checkSupportedVersions(String inputStr, List<String> supportedVersions) {
        for (String supportedVersion: supportedVersions) {
            if (inputStr.contains("/" + supportedVersion + "/")) {
                return true;
            }
        }
        return false;
    }

    /**
     * check if the token exists in the header.
     * 
     * @param authorizationHeader String
     * @return the boolean
     */
    protected boolean isValidJWT(final String authorizationHeader) {

        // Check if the Authorization header is valid
        // It must not be null and must be prefixed with "Bearer" plus a whitespace
        // Authentication scheme comparison must be case-insensitive
        return authorizationHeader != null
                && authorizationHeader.toLowerCase().startsWith(Constants.AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }

    /**
     * abort the request when unauthorized.
     * 
     * @param requestContext ContainerRequestContext
     * @throws IOException
     */
    protected void abortWithUnauthorized(final HttpServletResponse httpResp) throws IOException {
        httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                           "Authorization header must not be null and must be prefixed with \"Bearer\" plus a whitespace.");
    }

}
