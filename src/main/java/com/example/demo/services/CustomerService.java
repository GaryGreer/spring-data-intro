package com.example.demo.services;

import com.example.demo.controllers.CustomerController;
import com.example.demo.entities.CustomerEntity;
import com.example.demo.error.IdNotFoundException;
import com.example.demo.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.net.URI;
import java.util.Optional;

@Service
public class CustomerService implements ICustomerService, Serializable {

    @Autowired CustomerRepository customerRepository;

    @Override
    public ResponseEntity createCustomer(CustomerEntity customer) {
        customerRepository.save(customer);
        URI u = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(CustomerController.class).findCustomer(customer.getId())).toUri();
        return ResponseEntity.ok(u);
    }

    @Override
    public ResponseEntity<String> deleteCustomer(Long id) {
        Optional<CustomerEntity> deleteCustomer = customerRepository.findById(id);
        if (deleteCustomer.isPresent()){
            customerRepository.deleteById(id);
            return new ResponseEntity<>("customer deleted successfully.", new HttpHeaders(), HttpStatus.NO_CONTENT);
        } else {
            throw new IdNotFoundException(id);
        }
    }

    @Override
    public ResponseEntity<CustomerEntity> findById(Long id) {

        Optional<CustomerEntity> foundCustomer = customerRepository.findById(id);
        if (foundCustomer.isPresent()) {
            return new ResponseEntity<>(foundCustomer.get(), HttpStatus.OK);
        } else {
            throw new IdNotFoundException(id);
        }
    }

    @Override
    public Iterable<CustomerEntity> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Page findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }
}
