package com.example.authservice.domain;

public class Token {

    String Token;

    public Token(String token) {
        super();
        Token = token;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
