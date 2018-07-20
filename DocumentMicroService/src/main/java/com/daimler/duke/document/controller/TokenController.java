package com.daimler.duke.document.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daimler.duke.document.jwt.service.IJwtAuthService;

@RestController
// @Api(value = "Authenticate" + Constants.V1, description = "Authenticate JWT")
public class TokenController {

    @Autowired
    private IJwtAuthService iJwtAuthUtilService;

    @PostMapping(value = "/api/public/auth")
    public ResponseEntity<?> authenticate() {
        System.out.println("Entered to authenticate ->>");
        final String token = iJwtAuthUtilService.issueJwt();
        System.out.println(token);
        return ResponseEntity.ok(token);
    }

}
