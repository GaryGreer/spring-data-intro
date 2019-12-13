package com.example.demo.services;

import com.example.demo.entities.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ICustomerService {

    Optional<CustomerEntity> findById(Long id);
    CustomerEntity createCustomer(String name);
    void deleteCustomer(Long id);
    Iterable<CustomerEntity> getAllCustomers();
    Page<CustomerEntity> findAll(Pageable pageable);
}
