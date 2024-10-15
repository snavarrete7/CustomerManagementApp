package com.example.customerservice.controller;

import com.example.customerservice.domain.Customers;
import com.example.customerservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Iterator;
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

    @PostMapping
    public ResponseEntity<?> addCustomer(@RequestBody Customers newCustomer, UriComponentsBuilder uri){
        if(newCustomer.getName() == null || newCustomer.getEmail() == null || newCustomer.getPassword() == null){
            return ResponseEntity.badRequest().build();
        }
        Customers customerSaved = customerRepository.save(newCustomer);
        ResponseEntity<?> response = ResponseEntity.ok().build();
        return response;
    }


    @GetMapping("/search/{username}")
    public ResponseEntity<?> lookupCustomerByNameGet(@PathVariable("username") String username,
                                                     UriComponentsBuilder uri) {
        Iterator<Customers> customers = customerRepository.findAll().iterator();
        while(customers.hasNext()) {
            Customers cust = customers.next();
            if(cust.getName().equalsIgnoreCase(username)) {
                ResponseEntity<?> response = ResponseEntity.ok(cust);
                return response;
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable("id") long id){
        if(id == 0){
            return ResponseEntity.badRequest().build();
        }
        customerRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putCustomer(@PathVariable("id") long id, @RequestBody Customers newCustomer){
        return customerRepository.findById(id)
                .map(customer -> {
                    customer.setName(newCustomer.getName());
                    customer.setEmail(newCustomer.getEmail());
                    customer.setPassword(newCustomer.getPassword());
                    Customers updatedCustomer = customerRepository.save(customer);
                    return ResponseEntity.ok(updatedCustomer);
                })
                .orElse(ResponseEntity.notFound().build());
    }


}
