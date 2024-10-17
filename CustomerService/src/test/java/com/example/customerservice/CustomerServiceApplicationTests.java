package com.example.customerservice;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.example.customerservice.domain.AuthFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assertions.*;
import com.example.customerservice.domain.Customers;
import com.example.customerservice.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class CustomerServiceApplicationTests {

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	AuthFilter authFilter;

	@BeforeEach
	public void state(){
		List<Customers> customersList = customerRepository.findAll();
		if(customersList.size() > 20){
			Long id = 21L;
			customerRepository.deleteById(id);
		}
		if(customersList.size() < 20){
			Customers mock = new Customers();
			mock.setEmail("mock");
			mock.setId(9999);
			mock.setName("mock");
			mock.setPassword("xxxxxx");
			customerRepository.save(mock);
		}
	}

	@Test
	public void findAllCustomers(){
		List<Customers> customersList = customerRepository.findAll();
		Assertions.assertNotNull(customersList);
		Assertions.assertEquals(20, customersList.size());
	}

	@Test
	public void findById(){
		Long id = 1L;
		Optional<Customers> customer = customerRepository.findById(id);

		Customers mock = customer.get();
		String name = mock.getName();
		String pass = mock.getPassword();
		String email = mock.getEmail();

		Assertions.assertNotNull(customer);
		Assertions.assertNotNull(mock);
		Assertions.assertEquals(name, "Drugi");
		Assertions.assertEquals(pass, "rR1`{=WA756");
		Assertions.assertEquals(email, "drugi@example.com");

	}

	@Test
	public void invalidSaveUser(){
		Customers mock = null;
		boolean error = false;
		try{
			customerRepository.save(mock);
		}catch (Exception exception){
			error = true;
		}
		Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> {
			customerRepository.save(mock);});
        Assertions.assertTrue(error);

	}

	@Test
	public void validSaveUser(){
		Customers mock = new Customers();
		mock.setEmail("mock");
		mock.setId(9999);
		mock.setName("mock");
		mock.setPassword("xxxxxx");

		List<Customers> customersList = customerRepository.findAll();
		Assertions.assertEquals(20,customersList.size());

		boolean error = false;
		try{
			customerRepository.save(mock);
		}catch (Exception exception){
			error = true;
		}

		customersList = customerRepository.findAll();

		Assertions.assertFalse(error);
		Assertions.assertEquals(21,customersList.size());
	}

	@Test
	public void deleteCustomer(){
		List<Customers> customersList = customerRepository.findAll();
		Assertions.assertEquals(20,customersList.size());

		Long id = 20L;
		customerRepository.deleteById(id);

		customersList = customerRepository.findAll();
		Assertions.assertEquals(19,customersList.size());

	}

	@Test
	public void invalidToken(){
		String invalidToken = "this-is-an-invalid-token";
		List<String> scopeList = null;
		scopeList = Arrays.asList("read:customers");
		String validToken = createToken(scopeList);

		scopeList = authFilter.validateToken(invalidToken);
		Assertions.assertNull(scopeList);
	}

	@Test
	public void validToken(){
		List<String> scopeList = null;
		scopeList = Arrays.asList("read:customers");
		String validToken = createToken(scopeList);

		scopeList = authFilter.validateToken(validToken);
		Assertions.assertNotNull(scopeList);
		Assertions.assertTrue(scopeList.contains("read:customers"));
	}

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

}
