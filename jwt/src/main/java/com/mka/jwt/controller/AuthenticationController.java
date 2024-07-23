package com.mka.jwt.controller;

import com.mka.jwt.dto.JwtAuthenticationResponse;
import com.mka.jwt.dto.RefreshTokenRequest;
import com.mka.jwt.dto.SignInRequest;
import com.mka.jwt.dto.SignUpRequest;
import com.mka.jwt.entities.User;
import com.mka.jwt.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    @Autowired
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<User>  signUp(@RequestBody SignUpRequest signUpRequest){
        User user = authenticationService.signUP(signUpRequest);
        logger.info("User Added Successfully : "+signUpRequest.toString());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/signin")
    public  ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody SignInRequest signInRequest){
        return  ResponseEntity.ok(authenticationService.signin(signInRequest));
    }

    @GetMapping("/refresh")
    public  ResponseEntity<JwtAuthenticationResponse> resfresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return  ResponseEntity.ok(authenticationService.resfreshtoken(refreshTokenRequest));
    }
}
