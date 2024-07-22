package com.mka.jwt.services;

import com.mka.jwt.dto.JwtAuthenticationResponse;
import com.mka.jwt.dto.SignInRequest;
import com.mka.jwt.dto.SignUpRequest;
import com.mka.jwt.entities.User;

public interface AuthenticationService {
    public User signUP(SignUpRequest signUpRequest);

    public JwtAuthenticationResponse signin(SignInRequest signInRequest);
}
