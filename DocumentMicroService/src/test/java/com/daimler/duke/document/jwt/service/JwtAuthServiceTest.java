package com.daimler.duke.document.jwt.service;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.jsonwebtoken.Claims;

public class JwtAuthServiceTest {

    private IJwtAuthService iJwtAuthService;

    @Before
    public void init() throws Exception {

        iJwtAuthService = new JwtAuthService();
    }

    @Test
    public void testVerify() {
        String token = iJwtAuthService.issueJwt();
        Claims claims = iJwtAuthService.verify(token);

        Assert.assertNotNull(claims);
        String authGroup = (String) claims.get("role");
        assertThat("ApplicationId", authGroup, equalTo("authoGroup123"));
    }

    @Test
    public void testWrongTokenVerify() {
        try {
            String token = iJwtAuthService.issueJwt();
            iJwtAuthService.verify(token + "45454");
        }
        catch (Exception ex) {
            Assert.assertNotNull(ex);
            assertThat("ApplicationId",
                       ex.getMessage(),
                       containsString("JWT validity cannot be asserted and should not be trusted"));

        }
    }
}
