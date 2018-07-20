package com.daimler.duke.document.jwt.service;

import io.jsonwebtoken.Claims;

public interface IJwtAuthService {

    public String issueJwt();

    public Claims verify(final String token);
}
