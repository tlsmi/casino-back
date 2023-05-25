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

    public String newToken(String user) {
        String token = JWT.create()
                .withSubject(user)
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenExpiration))
                .sign(Algorithm.HMAC512(tokenSecret.getBytes(StandardCharsets.UTF_8)));
        return token;
    }

    public String getEmail(String token) {
        String user = JWT.require(Algorithm.HMAC512(tokenSecret.getBytes()))
                .build()
                .verify(token)
                .getSubject();
        return user;
    }

}
