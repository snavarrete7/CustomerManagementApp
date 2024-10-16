package com.example.authservice.controller;

import com.example.authservice.domain.Customers;
import com.example.authservice.domain.Token;
import com.example.authservice.service.JWTSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/token")
public class TokenController {

    public static Token appUserToken;

    @GetMapping
    public String getToken(){
        return "Token service running";
    }

    @PostMapping
    public ResponseEntity<?> createToken(@RequestBody Customers customer){
        String name = customer.getName();
        String pass = customer.getPassword();
        ResponseEntity<?> response = ResponseEntity.badRequest().build();

        if(name.equalsIgnoreCase("admin") && pass.equalsIgnoreCase("admin")){
            List<String> scopes = Arrays.asList("read:customers", "write:customers");
            String tokenString = JWTSupport.createToken(scopes);
            Token token = new Token(tokenString);
            response = ResponseEntity.ok(token);
        }else{
            if(checkCustomer(customer)){
                List<String> scopes = Arrays.asList("read:customers");
                String tokenString = JWTSupport.createToken(scopes);
                Token token = new Token(tokenString);
                response = ResponseEntity.ok(token);
            }

        }
        return response;
    }

    public boolean checkCustomer(Customers customer){
       Customers responseCustomer = null;
        try {
            URL url = new URL("http://localhost:8080/api/customers/search/" + customer.getName());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            List<String> scopes = Arrays.asList("read:customers", "write:customers");
            String tokenString = JWTSupport.createToken(scopes);
            Token token = new Token(tokenString);
            conn.setRequestProperty("authorization", "Bearer " + token.getToken());

            if (conn.getResponseCode() != 200) {
                return false;
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                String output = "";
                String out = "";
                while ((out = br.readLine()) != null) {
                    output += out;
                }
                conn.disconnect();
                ObjectMapper objectMapper = new ObjectMapper();
                responseCustomer = objectMapper.readValue(output, Customers.class);
            }

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        return responseCustomer.getName().equalsIgnoreCase(customer.getName()) && responseCustomer.getPassword().equalsIgnoreCase(customer.getPassword());
    }

}
