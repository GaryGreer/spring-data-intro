package com.example.demo

import com.example.demo.entities.BookingEntity
import com.example.demo.entities.CarEntity
import com.example.demo.entities.CustomerEntity
import com.example.demo.entities.LocationEntity
import com.example.demo.enums.Category
import com.example.demo.enums.Transmission
import com.example.demo.repositories.BookingRepository
import com.example.demo.repositories.CarRepository
import com.example.demo.repositories.CustomerRepository
import com.example.demo.repositories.LocationRepository
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

import java.time.LocalDate

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@ContextConfiguration
@SpringBootTest(classes = DemoApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingTests extends Specification {

    MockMvc mockMvc

    @Autowired LocationRepository locationRepository
    @Autowired CustomerRepository customerRepository
    @Autowired CarRepository carRepository
    @Autowired WebApplicationContext context
    @Autowired BookingRepository bookingRepository

    def setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
    }

    def "BookingEntity test"() {
        given: "an entity"
        def name = "Gary"
        def customer = new CustomerEntity(name)
        customerRepository.save(customer)

        def entity = new LocationEntity("Belfast")
        locationRepository.save(entity)

        def name_car = "Test Car"
        def reg = "12345"
        def manufacturer = "Ford"
        def model = "Fiesta"
        def transmission = Transmission.MANUAL
        def category = Category.DIESEL
        def car = new CarEntity(name_car, reg, manufacturer, model, transmission, category, entity)
        when: "BOOKING is created with given specifications"
        def start = LocalDate.now()
        def end = start.plusDays(1)
        def booking = new BookingEntity(start, end, car, customer)
        then: "entity attributes match getter"
        booking.getId() == 0
        booking.getStartDate() == start
        booking.getEndDate() == end
        booking.getCustomer().getName() == customer.getName()
        booking.getCar().getRegistration() == car.getRegistration()
    }

    def "BookingController - REST - adding valid booking test"() {
        given: "an entity"
        def entity = new LocationEntity("Cork")
        locationRepository.save(entity)

        def name = "Gary"
        def customer = new CustomerEntity(name)
        customerRepository.save(customer)

        def car_name = "Test Car"
        def reg = "12345"
        def manufacturer = "Ford"
        def model = "Fiesta"
        def transmission = Transmission.MANUAL
        def category = Category.DIESEL
        def car = new CarEntity(car_name, reg, manufacturer, model, transmission, category, entity)
        carRepository.save(car)

        def booking_json = '{"startDate":"2019-12-17","endDate":"2019-12-18","car":{"id":1,"name":"Test Car","registration":"12345","manufacturer":"Ford","model":"Fiesta","transmission":"MANUAL","category":"DIESEL","location":{"id":1,"location":"Cork"}},"customer":{"id":1,"name":"Gary"}}'


        when: "a valid post request to addBooking is made"
        def result = mockMvc.perform(post("/api/booking/").contentType(MediaType.APPLICATION_JSON).content(booking_json)).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "result is the URL to find the new booking"
        json == "http://localhost/api/booking/1"
    }

    def "BookingController - REST - adding invalid booking test - car"() {
        given: "an entity"
        def entity = new LocationEntity("Cork")
        locationRepository.save(entity)

        def name = "Gary"
        def customer = new CustomerEntity(name)
        customerRepository.save(customer)

        def booking_json = '{"startDate":"2019-12-17","endDate":"2019-12-18","car":{"id":1,"name":"Test Car","registration":"12345","manufacturer":"Ford","model":"Fiesta","transmission":"MANUAL","category":"DIESEL","location":{"id":1,"location":"Cork"}},"customer":{"id":1,"name":"Gary"}}'

        when: "an invalid post request to addBooking is made"
        def result = mockMvc.perform(post("/api/booking/").contentType(MediaType.APPLICATION_JSON).content(booking_json)).andReturn().response.status
        then: "correct status code returned"
        result == 404
    }

    def "BookingController - REST - adding invalid booking test - customer"() {
        given: "an entity"
        def entity = new LocationEntity("Cork")
        locationRepository.save(entity)

        def car_name = "Test Car"
        def reg = "12345"
        def manufacturer = "Ford"
        def model = "Fiesta"
        def transmission = Transmission.MANUAL
        def category = Category.DIESEL
        def car = new CarEntity(car_name, reg, manufacturer, model, transmission, category, entity)
        carRepository.save(car)

        def booking_json = '{"startDate":"2019-12-17","endDate":"2019-12-18","car":{"id":1,"name":"Test Car","registration":"12345","manufacturer":"Ford","model":"Fiesta","transmission":"MANUAL","category":"DIESEL","location":{"id":1,"location":"Cork"}},"customer":{"id":1,"name":"Gary"}}'

        when: "an invalid post request to addBooking is made"
        def result = mockMvc.perform(post("/api/booking/").contentType(MediaType.APPLICATION_JSON).content(booking_json)).andReturn().response.status
        then: "correct status code returned"
        result == 404
    }

    def "BookingController - REST - adding invalid booking test - location"() {
        given: "an entity"
        def entity = new LocationEntity("Cork")
        locationRepository.save(entity)

        def name = "Gary"
        def customer = new CustomerEntity(name)
        customerRepository.save(customer)

        def car_name = "Test Car"
        def reg = "12345"
        def manufacturer = "Ford"
        def model = "Fiesta"
        def transmission = Transmission.MANUAL
        def category = Category.DIESEL
        def car = new CarEntity(car_name, reg, manufacturer, model, transmission, category, entity)
        carRepository.save(car)

        def booking_json = '{"startDate":"2019-12-17","endDate":"2019-12-18","car":{"id":1,"name":"Test Car","registration":"12345","manufacturer":"Ford","model":"Fiesta","transmission":"MANUAL","category":"DIESEL","location":{"id":2,"location":"Belfast"}},"customer":{"id":1,"name":"Gary"}}'

        when: "an invalid post request to addBooking is made"
        def result = mockMvc.perform(post("/api/booking/").contentType(MediaType.APPLICATION_JSON).content(booking_json)).andReturn().response.status
        then: "correct status code returned"
        result == 404
    }

    def "BookingController - REST - adding invalid booking test - Car already booked"() {
        given: "an entity"
        def entity = new LocationEntity("Cork")
        locationRepository.save(entity)

        def name = "Gary"
        def customer = new CustomerEntity(name)
        customerRepository.save(customer)

        def name1 = "Michael"
        def customer1 = new CustomerEntity(name1)
        customerRepository.save(customer1)

        def car_name = "Test Car"
        def reg = "12345"
        def manufacturer = "Ford"
        def model = "Fiesta"
        def transmission = Transmission.MANUAL
        def category = Category.DIESEL
        def car = new CarEntity(car_name, reg, manufacturer, model, transmission, category, entity)
        carRepository.save(car)

        def booking_json = '{"startDate":"2019-12-16","endDate":"2019-12-19","car":{"id":1,"name":"Test Car","registration":"12345","manufacturer":"Ford","model":"Fiesta","transmission":"MANUAL","category":"DIESEL","location":{"id":1,"location":"Cork"}},"customer":{"id":1,"name":"Gary"}}'
        def booking_json1 = '{"startDate":"2019-12-17","endDate":"2019-12-18","car":{"id":1,"name":"Test Car","registration":"12345","manufacturer":"Ford","model":"Fiesta","transmission":"MANUAL","category":"DIESEL","location":{"id":1,"location":"Cork"}},"customer":{"id":2,"name":"Michael"}}'

        when: "a valid post request to addBooking is made"
        mockMvc.perform(post("/api/booking/").contentType(MediaType.APPLICATION_JSON).content(booking_json)).andReturn().response.contentAsString
        def result = mockMvc.perform(post("/api/booking/").contentType(MediaType.APPLICATION_JSON).content(booking_json1)).andReturn().response.status
        then: "result is the status code"
        result == 404
    }

    def "BookingController - REST - valid findById test"() {
        given: "an entity"
        def entity = new LocationEntity("Cork")
        locationRepository.save(entity)

        def name = "Gary"
        def customer = new CustomerEntity(name)
        customerRepository.save(customer)

        def car_name = "Test Car"
        def reg = "12345"
        def manufacturer = "Ford"
        def model = "Fiesta"
        def transmission = Transmission.MANUAL
        def category = Category.DIESEL
        def car = new CarEntity(car_name, reg, manufacturer, model, transmission, category, entity)
        carRepository.save(car)

        def start = LocalDate.now()
        def end = start.plusDays(1)
        def booking = new BookingEntity(start, end, car, customer)
        bookingRepository.save(booking)


        when: "a valid get request to findById is made"
        def result = mockMvc.perform(get("/api/booking/{id}", 1)).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "result is the URL to find the new location"
        json.id == 1
        json.startDate == start.toString()
        json.endDate == end.toString()
        json.car.id == car.getId()
        json.customer.id == customer.getId()
    }

    def "BookingController - REST - invalid findById test"() {
        given: "an entity"
        def entity = new LocationEntity("Cork")
        locationRepository.save(entity)

        def name = "Gary"
        def customer = new CustomerEntity(name)
        customerRepository.save(customer)

        def car_name = "Test Car"
        def reg = "12345"
        def manufacturer = "Ford"
        def model = "Fiesta"
        def transmission = Transmission.MANUAL
        def category = Category.DIESEL
        def car = new CarEntity(car_name, reg, manufacturer, model, transmission, category, entity)
        carRepository.save(car)

        def start = LocalDate.now()
        def end = start.plusDays(1)
        def booking = new BookingEntity(start, end, car, customer)
        bookingRepository.save(booking)


        when: "an invalid get request to findById is made"
        def result = mockMvc.perform(get("/api/booking/{id}", 2)).andReturn().response.status
        then: "result is the correct http status"
        result == 404
    }

    def "BookingController - REST - valid deleteById test"() {
        given: "an entity"
        def entity = new LocationEntity("Cork")
        locationRepository.save(entity)

        def name = "Gary"
        def customer = new CustomerEntity(name)
        customerRepository.save(customer)

        def car_name = "Test Car"
        def reg = "12345"
        def manufacturer = "Ford"
        def model = "Fiesta"
        def transmission = Transmission.MANUAL
        def category = Category.DIESEL
        def car = new CarEntity(car_name, reg, manufacturer, model, transmission, category, entity)
        carRepository.save(car)

        def start = LocalDate.now()
        def end = start.plusDays(1)
        def booking = new BookingEntity(start, end, car, customer)
        bookingRepository.save(booking)


        when: "a valid delete is made"
        def result = mockMvc.perform(delete("/api/booking/{id}", 1)).andReturn().response.contentAsString
        then: "result string confirms deletion"
        result == "booking deleted successfully."
    }

    def "BookingController - REST - invalid deleteById test"() {
        given: "an entity"
        def entity = new LocationEntity("Cork")
        locationRepository.save(entity)

        def name = "Gary"
        def customer = new CustomerEntity(name)
        customerRepository.save(customer)

        def car_name = "Test Car"
        def reg = "12345"
        def manufacturer = "Ford"
        def model = "Fiesta"
        def transmission = Transmission.MANUAL
        def category = Category.DIESEL
        def car = new CarEntity(car_name, reg, manufacturer, model, transmission, category, entity)
        carRepository.save(car)

        def start = LocalDate.now()
        def end = start.plusDays(1)
        def booking = new BookingEntity(start, end, car, customer)
        bookingRepository.save(booking)


        when: "an invalid delete request is made"
        def result = mockMvc.perform(delete("/api/booking/{id}", 2)).andReturn().response.status
        then: "result http status confirms invalid"
        result == 404
    }

    def "BookingController - REST - Page"() {
        given: "an entity"
        def entity = new LocationEntity("Cork")
        locationRepository.save(entity)

        def name = "Gary"
        def customer = new CustomerEntity(name)
        customerRepository.save(customer)

        def name1 = "Michael"
        def customer1 = new CustomerEntity(name1)
        customerRepository.save(customer1)

        def car_name = "Test Car"
        def reg = "12345"
        def manufacturer = "Ford"
        def model = "Fiesta"
        def transmission = Transmission.MANUAL
        def category = Category.DIESEL
        def car = new CarEntity(car_name, reg, manufacturer, model, transmission, category, entity)
        carRepository.save(car)

        def start = LocalDate.now()
        def end = start.plusDays(1)
        def booking = new BookingEntity(start, end, car, customer)
        bookingRepository.save(booking)

        def start1 = LocalDate.now().plusDays(2)
        def end1 = start1.plusDays(1)
        def booking1 = new BookingEntity(start1, end1, car, customer1)
        bookingRepository.save(booking1)

        when: "a valid call to listPageable is made"
        def result = mockMvc.perform(get("/api/booking/?page=0&size=2&sort=startDate,asc")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "result verifies the returned pageable"
        json.totalPages == 1
        json.totalElements == 2
        json.content[0].startDate == start.toString()
        json.content[0].car.id == car.getId()
        json.content[0].customer.name == customer.getName()
        json.content[1].startDate == start1.toString()
        json.content[1].car.id == car.getId()
        json.content[1].customer.name == customer1.getName()
        json.content.size == 2
    }

    def "BookingController - REST - Page2"() {
        given: "an entity"
        def entity = new LocationEntity("Cork")
        locationRepository.save(entity)

        def name = "Gary"
        def customer = new CustomerEntity(name)
        customerRepository.save(customer)

        def name1 = "Michael"
        def customer1 = new CustomerEntity(name1)
        customerRepository.save(customer1)

        def car_name = "Test Car"
        def reg = "12345"
        def manufacturer = "Ford"
        def model = "Fiesta"
        def transmission = Transmission.MANUAL
        def category = Category.DIESEL
        def car = new CarEntity(car_name, reg, manufacturer, model, transmission, category, entity)
        carRepository.save(car)

        def start = LocalDate.now()
        def end = start.plusDays(1)
        def booking = new BookingEntity(start, end, car, customer)
        bookingRepository.save(booking)

        def start1 = LocalDate.now().plusDays(2)
        def end1 = start1.plusDays(1)
        def booking1 = new BookingEntity(start1, end1, car, customer1)
        bookingRepository.save(booking1)

        when: "a valid call to listPageable is made"
        def result = mockMvc.perform(get("/api/booking/?page=0&size=1&sort=startDate,asc")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "result verifies the returned pageable"
        json.totalPages == 2
        json.totalElements == 2
        json.content[0].startDate == start.toString()
        json.content[0].car.id == car.getId()
        json.content[0].customer.name == customer.getName()
        json.content.size == 1
    }

}
