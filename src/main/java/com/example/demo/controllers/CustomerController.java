package com.example.demo.controllers;

import com.example.demo.entities.CustomerEntity;
import com.example.demo.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/api/customer")
@RequestMapping(value = "/api/customer")
public class CustomerController {

    @Autowired CustomerService customerService;

    @PostMapping(path = "/")
    public ResponseEntity<CustomerEntity> addCustomer(@RequestBody CustomerEntity customer){
        return customerService.createCustomer(customer);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CustomerEntity> findCustomer(@PathVariable("id") Long id){
        return customerService.findById(id);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable("id") Long id){
        return customerService.deleteCustomer(id);
    }

    @GetMapping(path = "/")
    public Page customerPageable(Pageable pageable){
        return customerService.findAll(pageable);
    }
}
