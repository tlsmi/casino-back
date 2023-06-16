package com.liceu.casino.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class TokenService {
    @Value("${token.secret}")
    String tokenSecret;

    @Value("${token.expiration}")
    int tokenExpiration;

    public String newToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenExpiration))
                .sign(Algorithm.HMAC512(tokenSecret.getBytes(StandardCharsets.UTF_8)));
    }

    public String getEmail(String token) {
        return JWT.require(Algorithm.HMAC512(tokenSecret.getBytes()))
                .build()
                .verify(token)
                .getSubject();
    }

}
