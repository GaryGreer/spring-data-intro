package com.example.demo.controllers;

import com.example.demo.entities.CustomerEntity;
import com.example.demo.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import org.springframework.web.util.UriComponentsBuilder;

@RestController("/api/customer")
@RequestMapping(value = "/api/customer")
public class CustomerController {

    @Autowired CustomerService customerService;

    @PutMapping(path = "/addCustomer")
    public ResponseEntity<CustomerEntity> addCustomer(@RequestParam(value="name") String name, UriComponentsBuilder ucBuilder){
        HttpHeaders headers = new HttpHeaders();
        CustomerEntity newCustomer = customerService.createCustomer(name);
        headers.setLocation(ucBuilder.path("/api/customer/addCustomer/{name}").buildAndExpand(newCustomer.getName()).toUri());
        return new ResponseEntity<>(newCustomer, headers, HttpStatus.CREATED);
    }

    @GetMapping(path = "/findById")
    public ResponseEntity<CustomerEntity> findCustomer(@RequestParam("id") Long id){
        Optional<CustomerEntity> foundCustomer = customerService.findById(id);
        if (foundCustomer.isPresent()){
            return new ResponseEntity<>(foundCustomer.get(), HttpStatus.OK);
        } else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id not found.");
        }
    }

    @DeleteMapping(path = "/deleteById")
    public ResponseEntity<String> deleteCustomer(@RequestParam("id") Long id){
        HttpHeaders headers = new HttpHeaders();
        Optional<CustomerEntity> deleteCustomer = customerService.findById(id);
        if (deleteCustomer.isPresent()){
            customerService.deleteCustomer(id);
            return new ResponseEntity<>("customer deleted successfully.", headers, HttpStatus.NO_CONTENT);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id not found.");
        }
    }

    @GetMapping(path = "/listPageable")
    public ResponseEntity<Page> customerPageable(Pageable pageable){
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(customerService.findAll(pageable), headers, HttpStatus.OK);
    }
}
