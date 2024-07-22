package com.mka.jwt.services;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.Objects;

public interface JWTService {

    public  String genarateToke(UserDetails userDetails);
    public String extractUserName(String token);

    String genarateRefreshToken(Map<String, Objects> extraclaims, UserDetails userDetails);

    public boolean isTokenValid(String token, UserDetails userDetails);

}
