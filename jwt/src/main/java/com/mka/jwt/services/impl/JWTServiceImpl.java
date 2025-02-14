package com.mka.jwt.services.impl;

import com.mka.jwt.services.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JWTServiceImpl implements JWTService {
    @Override
    public  String genarateToke(UserDetails userDetails){
        return Jwts.builder().setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60))
                .signWith(getSignkey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public  String genarateRefreshToken(Map<String, Objects> extraclaims, UserDetails userDetails){
        return Jwts.builder().setClaims(extraclaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60480000))
                .signWith(getSignkey(), SignatureAlgorithm.HS256)
                .compact();
    }
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username =  extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    @Override
    public String extractUserName(String token){
        return extractClaim(token, Claims::getSubject);
    }

    private Claims extractClaims(String token) {
        return  Jwts.parserBuilder().setSigningKey(getSignkey()).build().parseClaimsJws(token).getBody();
    }


    private Key getSignkey() {
        byte[] key = Decoders.BASE64.decode("fht6Y8W2vNcEKhRToP8xkm3RnDJDlFSTOS9k42j0+EY=");
        return Keys.hmacShaKeyFor(key);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers){
        final Claims claims = extractClaims(token);
        return claimsResolvers.apply(claims);
    }

}
