package com.example.authservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.example.authservice.domain.Customers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JWTSupport {

    public static String createToken(List<String> scopes) {
        try {
            String scopeString = String.join(" ", scopes);
            long timeExp = 1000 * 60 * 60 * 2; //2 hours
            Date expireDate = new Date(System.currentTimeMillis() + timeExp);

            String token = JWT.create()
                    .withClaim("scopes", scopeString)
                    .withExpiresAt(expireDate)
                    .sign(Algorithm.HMAC256("secret"));
            return token;

        } catch (JWTCreationException exception){
            return null;
        }
    }

    public static String convertCustomerToJson(Customers customer) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(customer);
        return jsonString;
    }

}
