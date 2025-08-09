package com.alura.forohub.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expirationHours:8}")
    private long expirationHours;

    public String generarToken(String subject) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        Instant expiresAt = Instant.now().plus(expirationHours, ChronoUnit.HOURS);
        return JWT.create()
                .withIssuer("forohub")
                .withSubject(subject)
                .withExpiresAt(Date.from(expiresAt))
                .sign(algorithm);
    }

    public String getSubject(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        DecodedJWT decoded = JWT.require(algorithm)
                .withIssuer("forohub")
                .build()
                .verify(token);
        return decoded.getSubject();
    }
}
