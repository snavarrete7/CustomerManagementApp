package com.example.customerservice.domain;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import com.auth0.jwt.interfaces.Claim;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@Order(1)
public class AuthFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        String token = "";

        if(header != null && header.startsWith("Bearer ")){
            token = header.substring(7);
            List<String> scopes = validateToken(token);

            if (request.getMethod().equals("GET") && request.getRequestURI().equals("/api/customers")) {
                if (!scopes.contains("read:customers")) {
                    response.sendError(401, "Acces denied");
                    return;
                }
            }

            if (request.getRequestURI().startsWith("/api/customers") && (request.getMethod().equals("PUT") || request.getMethod().equals("POST") || request.getMethod().equals("DELETE"))) {
                if (!scopes.contains("read:customers") || !scopes.contains("write:customers")) {
                    response.sendError(401, "Acces denied");
                    return;
                }
            }

        }else{
            response.sendError(401, "No token provided");
            return;
        }

        filterChain.doFilter(request, response);
    }

    public List<String> validateToken(String token){
        List<String> scopeList = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            DecodedJWT jwt = JWT.require(algorithm)
                    .build()
                    .verify(token);

            String scopes = jwt.getClaim("scopes").asString();
            scopeList = Arrays.asList(scopes.split(" "));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return scopeList;
    }

}
