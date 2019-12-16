package com.example.demo.services;

import com.example.demo.entities.CustomerEntity;
import com.example.demo.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CustomerService implements ICustomerService {

    @Autowired CustomerRepository customerRepository;

    @Override
    public CustomerEntity createCustomer(String name) {
        CustomerEntity newCustomer = new CustomerEntity(name);
        customerRepository.save(newCustomer);
        return newCustomer;
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public Optional<CustomerEntity> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Iterable<CustomerEntity> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Page<CustomerEntity> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }
}
