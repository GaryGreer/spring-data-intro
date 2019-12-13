package com.example.demo

import com.example.demo.entities.CustomerEntity
import com.example.demo.repositories.CustomerRepository
import com.example.demo.services.CustomerService
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.server.ResponseStatusException
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

    def "CustomerService - tests"() {
        given: "an entity"
        def id1 = 1
        def name1 = "Gary"
        when: "createCustomer service is called"
        def c1 = customerService.createCustomer(name1)
        then: "customerEntity matches name"
        c1.getName() == name1
        c1.getId() == id1

        when: "findById service is called with a valid id"
        def result = customerService.findById(id1)
        then: "result is an entity with an id"
        result.get().id == id1
        result.get().id > 0


        def id2 = 2
        when: "findById service is called with an invalid id"
        def invalidResult = customerService.findById(id2)
        then: "result is an empty optional"
        invalidResult == Optional.empty()

        def name2 = "Michael"
        def c2 = customerService.createCustomer(name2)
        when: "getAllCustomers service is called"
        def allCustomersResult = customerService.getAllCustomers()
        then: "check results are valid"
        !allCustomersResult.isEmpty()
        and: "includes both added customers"
        id1 in allCustomersResult[0].id
        id2 in allCustomersResult[1].id


        def name3 = "Victoria"
        def c3 = customerService.createCustomer(name3)
        def pageNo = 0
        def pageSize = 3
        def totalPages = 1
        def totalElements = 3
        Pageable testPage = PageRequest.of(pageNo, pageSize, Sort.by("name").descending())
        when: "findAll(Pageable) service is called"
        def pageableResult = customerService.findAll(testPage)
        then: "pageable result should be valid"
        pageableResult.totalElements == totalElements
        pageableResult.totalPages == totalPages
        pageableResult.content[0].getName() == name3
        pageableResult.content[1].getName() == name2
        pageableResult.content[2].getName() == name1
    }

    def "CustomerController - REST - adding valid customer test"() {
        given: "an entity"
        when: "a valid call to addCustomer is made"
        def name = "Gary"
        def result = mockMvc.perform(put("/api/customer/addCustomer?name=Gary")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "result is an entity with an id"
        json.id == 1
        json.id > 0
        json.name == name
    }

    def "CustomerController - REST - valid findById"() {
        given: "an entity"
        def name = "Gary"
        def c1 = customerRepository.save(new CustomerEntity(name))
        when: "a valid call to addCustomer is made"
        def result = mockMvc.perform(get("/api/customer/findById?id=1")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "result is an entity with an id"
        json.id == 1
        json.name == name
    }

    def "CustomerController - REST - valid deleteById"() {
        given: "an entity"
        def name = "Gary"
        def c1 = customerRepository.save(new CustomerEntity(name))
        when: "a valid call to deleteById is made"
        def result = mockMvc.perform(delete("/api/customer/deleteById?id=1")).andReturn().response.contentAsString
        then: "result verifies the delete"
        result == "customer deleted successfully."
    }

    def "CustomerController - REST - invalid deleteById"() {
        given: "an entity"
        def name = "Gary"
        def c1 = customerRepository.save(new CustomerEntity(name))
        when: "an invalid call to deleteById is made"
        def except = mockMvc.perform(delete("/api/customer/deleteById?id=2")).andReturn().getResolvedException().getClass()
        then: "response status exception thrown"
        assert(except.is(ResponseStatusException))
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
        def result = mockMvc.perform(get("/api/customer/listPageable?page=0&size=3&sort=name,desc")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "result verifies the returned pageable"
        json.totalPages == 1
        json.totalElements == 3
        json.content[0].name == "Victoria"
        json.content[1].name == "Michael"
        json.content[2].name == "Gary"
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
        def result = mockMvc.perform(get("/api/customer/listPageable?page=0&size=1&sort=name,desc")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "result verifies the returned pageable"
        json.totalPages == 3
        json.totalElements == 3
        json.content[0].name == "Victoria"
    }
}