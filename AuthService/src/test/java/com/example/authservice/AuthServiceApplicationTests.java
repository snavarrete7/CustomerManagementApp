package com.example.authservice;

import com.example.authservice.controller.TokenController;
import com.example.authservice.domain.Customers;
import com.example.authservice.service.JWTSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class AuthServiceApplicationTests {

	@Autowired
	JWTSupport jwtSupport;

	@Autowired
	TokenController tokenController;

	@Test
	public void createToken(){
		List<String> scopeList = null;
		scopeList = Arrays.asList("read:customers");
		String validToken = jwtSupport.createToken(scopeList);
		Assertions.assertNotNull(validToken);
	}


}
