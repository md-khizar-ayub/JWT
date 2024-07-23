package com.mka.jwt.services.impl;

import com.mka.jwt.dto.JwtAuthenticationResponse;
import com.mka.jwt.dto.RefreshTokenRequest;
import com.mka.jwt.dto.SignInRequest;
import com.mka.jwt.dto.SignUpRequest;
import com.mka.jwt.entities.Role;
import com.mka.jwt.entities.User;
import com.mka.jwt.repository.UserRepository;
import com.mka.jwt.services.AuthenticationService;
import com.mka.jwt.services.JWTService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private final AuthenticationManager   authenticationManager;
    private final JWTService jwtService ;
    @Override
    public User signUP(SignUpRequest signUpRequest){
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setFirstname(signUpRequest.getFirstName());
        user.setSecondname(signUpRequest.getLastName());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
       return userRepository.save(user);

    }

    @Override
    @Cacheable("signin")
    public JwtAuthenticationResponse signin(SignInRequest signInRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));

        User user = userRepository.findByEmail(signInRequest.getEmail()).orElseThrow(()-> new UsernameNotFoundException("User Not Found"));
       var jwt = jwtService.genarateToke(user);
       var refreshToken = jwtService.genarateRefreshToken(new HashMap<>(), user);

       JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
       jwtAuthenticationResponse.setToken(jwt);
       jwtAuthenticationResponse.setResfreshToken(refreshToken);
        return jwtAuthenticationResponse;
    }

    @Override
    public JwtAuthenticationResponse resfreshtoken(RefreshTokenRequest refreshTokenRequest) {
        String userEmail = jwtService.extractUserName(refreshTokenRequest.getToken());
        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new UsernameNotFoundException("Refresh Token is not Valid"));
        if(jwtService.isTokenValid(refreshTokenRequest.getToken(), user)){
            var jwt = jwtService.genarateToke(user);
            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setResfreshToken(refreshTokenRequest.getToken());
            return jwtAuthenticationResponse;
        }
        return null;
    }


}
