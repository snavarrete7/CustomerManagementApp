package com.example.authservice.controller;

import com.example.authservice.domain.Customers;
import com.example.authservice.service.JWTSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @GetMapping
    public String root(){
        return "Register Controller running";
    }


    @PostMapping
    public ResponseEntity<?> registerNewCustomer(@RequestBody Customers customer){
        ResponseEntity response = ResponseEntity.badRequest().build();

        if(customer.getName() == null && customer.getPassword() == null && customer.getEmail() == null){
            return ResponseEntity.badRequest().build();
        }else {

            String jsonCustomer = "";
            try{
                jsonCustomer = JWTSupport.convertCustomerToJson(customer);
            }catch (IOException e){
                e.printStackTrace();
            }

            try{
                URL url = new URL("http://localhost:8080/api/customers");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                List<String> scopes = null;
                if(customer.getName().equalsIgnoreCase("admin")){
                    scopes = Arrays.asList("read:customers", "write:customers");
                }else{
                    scopes = Arrays.asList("read:customers");
                }

                scopes = Arrays.asList("read:customers", "write:customers");
                String tokenString = JWTSupport.createToken(scopes);
                conn.setRequestProperty("Authorization", "Bearer " + tokenString);


                OutputStream os = conn.getOutputStream();
                os.write(jsonCustomer.getBytes());
                os.flush();

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    response = ResponseEntity.ok("Customer created");
                } else {
                    response = ResponseEntity.badRequest().build();
                }
                conn.disconnect();

            }catch (IOException e){
                e.printStackTrace();
            }

        }

        return response;
    }

}
