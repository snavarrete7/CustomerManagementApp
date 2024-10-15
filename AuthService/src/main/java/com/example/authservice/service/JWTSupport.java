package com.example.authservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JWTSupport {

    public static String createToken(List<String> scopes) {
        try {
            String scopeString = String.join(" ", scopes);
            Algorithm algorithm = Algorithm.HMAC256("secret");
            long fiveHoursInMillis = 1000 * 60 * 60 * 5;
            Date expireDate = new Date(System.currentTimeMillis() + fiveHoursInMillis);
            String token = JWT.create()
                    .withSubject("apiuser")
                    .withIssuer("me@me.com")
                    .withClaim("scopes", scopeString)
                    .withExpiresAt(expireDate)
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            return null;
        }
    }

}
