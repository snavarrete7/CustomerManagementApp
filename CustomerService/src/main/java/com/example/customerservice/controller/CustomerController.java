package com.example.customerservice.controller;

import com.example.customerservice.domain.Customers;
import com.example.customerservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository;

    @GetMapping
    public List<Customers> getAllCustomers(){
        return customerRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Customers> getById(@PathVariable("id") long id){
        Optional<Customers> customer = customerRepository.findById(id);
        return customer;
    }

}
