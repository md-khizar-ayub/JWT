package com.mka.jwt.services.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTServiceImpl {
    public  String genarateToke(UserDetails userDetails){
        return Jwts.builder().setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60))
                .signWith(getSignkey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers){
        final Claims claims = extractClaims(token);
        return claimsResolvers.apply(claims);
    }

    public String extractUserName(String token){
        return extractClaim(token, Claims::getSubject);
    }

    private Claims extractClaims(String token) {
        return  Jwts.parserBuilder().setSigningKey(getSignkey()).build().parseClaimsJws(token).getBody();
    }


    private Key getSignkey() {
        byte[] key = Decoders.BASE64.decode("KhizarKhizarKhizarKhizarKhizarKhizarKhizar");
        return Keys.hmacShaKeyFor(key);
    }

}
