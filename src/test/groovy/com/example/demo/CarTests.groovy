package com.example.demo

import com.example.demo.entities.CarEntity
import com.example.demo.entities.LocationEntity
import com.example.demo.enums.Category
import com.example.demo.enums.Transmission
import com.example.demo.repositories.CustomerRepository
import com.example.demo.repositories.LocationRepository
import com.example.demo.services.CarService
import com.example.demo.services.LocationService
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@ContextConfiguration
@SpringBootTest(classes = DemoApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CarTests extends Specification {

    MockMvc mockMvc

    @Autowired CustomerRepository carRepository
    @Autowired CarService carService
    @Autowired LocationRepository locationRepository
    @Autowired LocationService locationService
    @Autowired WebApplicationContext context

    def setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
    }

    def "CarEntity test"() {
        given: "an entity"
        def name = "Test Car"
        def reg = "12345"
        def manufacturer = "Ford"
        def model = "Fiesta"
        def transmission = Transmission.MANUAL
        def category = Category.DIESEL
        def loc_id = 1
        def entity = new LocationEntity("Test")
        locationRepository.save(entity)

        when: "car is created with given specifications"
        def car = new CarEntity(name, reg, manufacturer, model, transmission, category, entity)
        then: "entity attributes match getter"
        name == car.getName()
        0 == car.getId()
        reg == car.getRegistration()
        manufacturer == car.getManufacturer()
        model == car.getModel()
        transmission == car.getTransmission()
        category == car.getCategory()
        loc_id == car.getLocation().getId()
    }

    def "CarController - REST - adding valid car test"() {
        given: "an entity"
        def entity = new LocationEntity("Cork")
        locationRepository.save(entity)
        def car_json = '{"name": "Test Car", "registration": "192-kk-12345", "manufacturer": "Tesla", "model": "S", "transmission": "AUTOMATIC", "category": "ELECTRIC", "location": {"id": 1, "location": "Cork"}}'
        when: "a valid post request to add car is made"
        def result = mockMvc.perform(post("/api/car/").contentType(MediaType.APPLICATION_JSON).content(car_json)).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "result is the URL to find the new location"
        json == "http://localhost/api/car/1"
    }

    def "CarController - REST - adding invalid car test"() {
        given: "an entity"
        def entity = new LocationEntity("Cork")
        locationRepository.save(entity)
        def car_json = '{"name": "Test Car", "registration": "192-kk-12345", "manufacturer": "Tesla", "model": "S", "transmission": "AUTOMATIC", "category": "ELECTRIC", "location": {"id": 1, "location": "Cork"}}'
        when: "a valid post request to add car is made"
        mockMvc.perform(post("/api/car/").contentType(MediaType.APPLICATION_JSON).content(car_json))
        def result = mockMvc.perform(post("/api/car/").contentType(MediaType.APPLICATION_JSON).content(car_json)).andReturn().response.status
        then: "status code is 404"
        result == 404
    }

    def "CarController - REST - valid findById"() {
        given: "an entity"
        def entity = new LocationEntity("Cork")
        def name = "Test Car"
        def id = 1
        def registration = "192-kk-12345"
        def manufacturer = "Tesla"
        def model = "S"
        def transmission = "AUTOMATIC"
        def category = "ELECTRIC"
        def location_id = 1
        def location = "Cork"
        locationRepository.save(entity)
        def car_json = '{"name": "Test Car", "registration": "192-kk-12345", "manufacturer": "Tesla", "model": "S", "transmission": "AUTOMATIC", "category": "ELECTRIC", "location": {"id": 1, "location": "Cork"}}'
        mockMvc.perform(post("/api/car/").contentType(MediaType.APPLICATION_JSON).content(car_json))
        when: "a valid call to findCar is made"
        def result = mockMvc.perform(get("/api/car/{id}", 1)).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "result is an entity with an id"
        json.id == id
        json.name == name
        json.registration == registration
        json.manufacturer == manufacturer
        json.model == model
        json.transmission == transmission
        json.category == category
        json.location.id == location_id
        json.location.location == location
    }

    def "CarController - REST - invalid findById"() {
        given: "an entity"
        def entity = new LocationEntity("Cork")
        locationRepository.save(entity)
        def car_json = '{"name": "Test Car", "registration": "192-kk-12345", "manufacturer": "Tesla", "model": "S", "transmission": "AUTOMATIC", "category": "ELECTRIC", "location": {"id": 1, "location": "Cork"}}'
        mockMvc.perform(post("/api/car/").contentType(MediaType.APPLICATION_JSON).content(car_json))
        when: "an invalid call to findCar is made"
        def result = mockMvc.perform(get("/api/car/{id}", 2)).andReturn().response.status
        then: "status code is 404"
        result == 404
    }

    def "CarController - REST - Page"() {
        given: "an entity"
        def entity = new LocationEntity("Cork")
        locationRepository.save(entity)
        def car_json = '{"name": "Test Car 1", "registration": "192-KK-12345", "manufacturer": "Tesla", "model": "S", "transmission": "AUTOMATIC", "category": "ELECTRIC", "location": {"id": 1, "location": "Cork"}}'
        def car_json1 = '{"name": "Test Car 2", "registration": "192-D-12345", "manufacturer": "Tesla", "model": "S", "transmission": "AUTOMATIC", "category": "ELECTRIC", "location": {"id": 1, "location": "Cork"}}'
        def car_json2 = '{"name": "Test Car 3", "registration": "192-C-12345", "manufacturer": "Tesla", "model": "S", "transmission": "AUTOMATIC", "category": "ELECTRIC", "location": {"id": 1, "location": "Cork"}}'
        mockMvc.perform(post("/api/car/").contentType(MediaType.APPLICATION_JSON).content(car_json))
        mockMvc.perform(post("/api/car/").contentType(MediaType.APPLICATION_JSON).content(car_json1))
        mockMvc.perform(post("/api/car/").contentType(MediaType.APPLICATION_JSON).content(car_json2))
        when: "a valid call to listPageable is made"
        def result = mockMvc.perform(get("/api/car/?page=0&size=3&sort=name,desc")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "result verifies the returned pageable"
        json.totalPages == 1
        json.totalElements == 3
        json.content[0].name == "Test Car 3"
        json.content[1].name == "Test Car 2"
        json.content[2].name == "Test Car 1"
        json.content.size == 3
    }

    def "CarController - REST - Page2"() {
        given: "an entity"
        def entity = new LocationEntity("Cork")
        locationRepository.save(entity)
        def car_json = '{"name": "Test Car 1", "registration": "192-KK-12345", "manufacturer": "Tesla", "model": "S", "transmission": "AUTOMATIC", "category": "ELECTRIC", "location": {"id": 1, "location": "Cork"}}'
        def car_json1 = '{"name": "Test Car 2", "registration": "192-D-12345", "manufacturer": "Tesla", "model": "S", "transmission": "AUTOMATIC", "category": "ELECTRIC", "location": {"id": 1, "location": "Cork"}}'
        def car_json2 = '{"name": "Test Car 3", "registration": "192-C-12345", "manufacturer": "Tesla", "model": "S", "transmission": "AUTOMATIC", "category": "ELECTRIC", "location": {"id": 1, "location": "Cork"}}'
        mockMvc.perform(post("/api/car/").contentType(MediaType.APPLICATION_JSON).content(car_json))
        mockMvc.perform(post("/api/car/").contentType(MediaType.APPLICATION_JSON).content(car_json1))
        mockMvc.perform(post("/api/car/").contentType(MediaType.APPLICATION_JSON).content(car_json2))
        when: "a valid call to findByLocation is made"
        def result = mockMvc.perform(get("/api/car/?page=0&size=1&sort=name,desc")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "result verifies the returned pageable"
        json.totalPages == 3
        json.totalElements == 3
        json.content[0].name == "Test Car 3"
        json.content.size == 1
    }

    def "CarController - REST - Find by location test"() {
        given: "an entity"
        def entity = new LocationEntity("Cork")
        locationRepository.save(entity)
        def entity2 = new LocationEntity("Belfast")
        locationRepository.save(entity2)
        def car_json = '{"name": "Test Car 1", "registration": "192-KK-12345", "manufacturer": "Tesla", "model": "S", "transmission": "AUTOMATIC", "category": "ELECTRIC", "location": {"id": 1, "location": "Cork"}}'
        def car_json1 = '{"name": "Test Car 2", "registration": "192-D-12345", "manufacturer": "Tesla", "model": "S", "transmission": "AUTOMATIC", "category": "ELECTRIC", "location": {"id": 1, "location": "Cork"}}'
        def car_json2 = '{"name": "Test Car 2", "registration": "192-D-12345", "manufacturer": "Tesla", "model": "S", "transmission": "AUTOMATIC", "category": "ELECTRIC", "location": {"id": 2, "location": "Belfast"}}'
        mockMvc.perform(post("/api/car/").contentType(MediaType.APPLICATION_JSON).content(car_json))
        mockMvc.perform(post("/api/car/").contentType(MediaType.APPLICATION_JSON).content(car_json1))
        mockMvc.perform(post("/api/car/").contentType(MediaType.APPLICATION_JSON).content(car_json2))
        when: "a valid call to listPageable is made"
        def result = mockMvc.perform(get("/api/car/location/?{location}", "Belfast")).andReturn().response.contentAsString
        print("Result -------------------->" + result)
        def json = new JsonSlurper().parseText(result)
        then: "result verifies the returned pageable"
        json.totalPages == 3
        json.totalElements == 3
        json.content[0].name == "Test Car 3"
        json.content.size == 1
    }
}
