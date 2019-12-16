package com.example.demo.services;

import com.example.demo.entities.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.net.URI;

public interface ICustomerService {

    ResponseEntity<CustomerEntity> findById(Long id);
    ResponseEntity<URI> createCustomer(CustomerEntity customer);
    ResponseEntity<String> deleteCustomer(Long id);
    Iterable<CustomerEntity> getAllCustomers();
    Page findAll(Pageable pageable);
}
