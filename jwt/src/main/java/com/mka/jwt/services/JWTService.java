package com.mka.jwt.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {

    public  String genarateToke(UserDetails userDetails);
    public String extractUserName(String token);

    public boolean isTokenValid(String token, UserDetails userDetails);
}
