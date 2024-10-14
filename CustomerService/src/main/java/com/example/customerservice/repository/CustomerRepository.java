package com.example.customerservice.repository;

import com.example.customerservice.domain.Customers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customers, Long> {
}
