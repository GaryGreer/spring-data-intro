package com.example.demo

import com.example.demo.entities.CustomerEntity
import com.example.demo.repositories.CustomerRepository
import com.example.demo.services.CustomerService
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

@ContextConfiguration
@SpringBootTest(classes = DemoApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CustomerTests extends Specification {

    MockMvc mockMvc

    @Autowired CustomerRepository customerRepository
    @Autowired CustomerService customerService
    @Autowired WebApplicationContext context

    def setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
    }

    def "CustomerEntity test"() {
        given: "an entity"
        def name = "Gary"
        when: "customer is created with given name"
        def customer = new CustomerEntity(name)
        then: "entity attributes match getter"
        name == customer.getName()
        0 == customer.getId()
    }

    def "CustomerController - REST - adding valid customer test"() {
        given: "an entity"
        when: "a valid call to addCustomer is made"
        def name = "Gary"
        def result = mockMvc.perform(post("/api/customer/").contentType(MediaType.APPLICATION_JSON).content('{"name" : "Gary"}')).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "result is the URL to find the new location"
        json == "http://localhost/api/customer/1"
    }

    def "CustomerController - REST - valid findById"() {
        given: "an entity"
        def name = "Gary"
        def c1 = customerRepository.save(new CustomerEntity(name))
        when: "a valid call to addCustomer is made"
        def result = mockMvc.perform(get("/api/customer/{id}", 1)).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "result is an entity with an id"
        json.id == 1
        json.name == name
    }

    def "CustomerController - REST - invalid findById"() {
        given: "an entity"
        def name = "Gary"
        def c1 = customerRepository.save(new CustomerEntity(name))
        when: "a valid call to addCustomer is made"
        def result = mockMvc.perform(get("/api/customer/{id}", 2)).andReturn().response.status
        then: "status code is 404"
        result == 404
    }

    def "CustomerController - REST - valid deleteById"() {
        given: "an entity"
        def name = "Gary"
        def c1 = customerRepository.save(new CustomerEntity(name))
        when: "a valid call to deleteById is made"
        def result = mockMvc.perform(delete("/api/customer/{id}", 1)).andReturn().response.contentAsString
        then: "result verifies the delete"
        result == "customer deleted successfully."
    }

    def "CustomerController - REST - invalid deleteById"() {
        given: "an entity"
        def name = "Gary"
        def c1 = customerRepository.save(new CustomerEntity(name))
        when: "an invalid call to deleteById is made"
        def result = mockMvc.perform(delete("/api/customer/{id}",2)).andReturn().response.status
        then: "status code is 404"
        result == 404
    }

    def "CustomerController - REST - Page"() {
        given: "an entity"
        def name1 = "Gary"
        def name2 = "Michael"
        def name3 = "Victoria"
        def c1 = customerRepository.save(new CustomerEntity(name1))
        def c2 = customerRepository.save(new CustomerEntity(name2))
        def c3 = customerRepository.save(new CustomerEntity(name3))
        when: "a valid call to listPageable is made"
        def result = mockMvc.perform(get("/api/customer/?page=0&size=3&sort=name,desc")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "result verifies the returned pageable"
        json.totalPages == 1
        json.totalElements == 3
        json.content[0].name == "Victoria"
        json.content[1].name == "Michael"
        json.content[2].name == "Gary"
        json.content.size == 3
    }

    def "CustomerController - REST - Page2"() {
        given: "an entity"
        def name1 = "Gary"
        def name2 = "Michael"
        def name3 = "Victoria"
        def c1 = customerRepository.save(new CustomerEntity(name1))
        def c2 = customerRepository.save(new CustomerEntity(name2))
        def c3 = customerRepository.save(new CustomerEntity(name3))
        when: "a valid call to listPageable is made"
        def result = mockMvc.perform(get("/api/customer/?page=0&size=1&sort=name,desc")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "result verifies the returned pageable"
        json.totalPages == 3
        json.totalElements == 3
        json.content[0].name == "Victoria"
        json.content.size == 1
    }
}