package com.example.demo

import com.example.demo.entities.LocationEntity
import com.example.demo.repositories.LocationRepository
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

@ContextConfiguration
@SpringBootTest(classes = DemoApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LocationTests extends Specification {

    MockMvc mockMvc

    @Autowired LocationRepository locationRepository
    @Autowired LocationService locationService
    @Autowired WebApplicationContext context

    def setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
    }


    def "LocationEntity test"() {
        given: "an entity"
        def location = "Belfast"
        when: "entity is created with given location"
        def entity = new LocationEntity(location)
        then: "entity attributes match getter"
        location == entity.getLocation()
        0 == entity.getId()
    }

    def "LocationController - REST - adding valid location test"() {
        given: "an entity"
        when: "a valid call to addLocation is made"
        def result = mockMvc.perform(post("/api/location/").contentType(MediaType.APPLICATION_JSON).content('{"location" : "Belfast"}')).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "result is the URL to find the new location"
        json == "http://localhost/api/location/1"
    }

    def "LocationController - REST - adding invalid location test"() {
        given: "an entity"
        when: "an invalid call to addLocation is made"
        mockMvc.perform(post("/api/location/").contentType(MediaType.APPLICATION_JSON).content('{"location" : "Belfast"}'))
        def result = mockMvc.perform(post("/api/location/").contentType(MediaType.APPLICATION_JSON).content('{"location" : "Belfast"}')).andReturn().response.status
        then: "status code is 404"
        result == 404
    }

    def "LocationController - REST - valid findById"() {
        given: "an entity"
        def loc = "Cork"
        def e1 = locationRepository.save(new LocationEntity(loc))
        when: "a valid call to findById is made"
        def result = mockMvc.perform(get("/api/location/{id}", 1)).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "result is an entity with an id"
        json.id == 1
        json.location == loc
    }

    def "LocationController - REST - invalid findById"() {
        given: "an entity"
        def loc = "Cork"
        def e1 = locationRepository.save(new LocationEntity(loc))
        when: "an invalid call to findById is made"
        def result = mockMvc.perform(get("/api/location/{id}", 2)).andReturn().response.status
        then: "status code is 404"
        result == 404
    }

    def "LocationController - REST - valid deleteById"() {
        given: "an entity"
        def loc = "Cork"
        def e1 = locationRepository.save(new LocationEntity(loc))
        when: "a valid call to deleteLocation is made"
        def result = mockMvc.perform(delete("/api/location/{id}", 1)).andReturn().response.contentAsString
        then: "result string confirms deletion"
        result == 'location deleted successfully.'
    }

    def "LocationController - REST - invalid deleteById"() {
        given: "an entity"
        def loc = "Cork"
        def e1 = locationRepository.save(new LocationEntity(loc))
        when: "a valid call to deleteLocation is made"
        def result = mockMvc.perform(delete("/api/location/{id}", 2)).andReturn().response.status
        then: "status code is 404"
        result == 404
    }

    def "LocationController - REST - Page"() {
        given: "an entity"
        def loc1 = "Belfast"
        def loc2 = "Cork"
        def loc3 = "Portrush"
        def e1 = locationRepository.save(new LocationEntity(loc1))
        def e2 = locationRepository.save(new LocationEntity(loc2))
        def e3 = locationRepository.save(new LocationEntity(loc3))
        when: "a valid call to listPageable is made"
        def result = mockMvc.perform(get("/api/location/?page=0&size=3&sort=location,desc")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "result verifies the returned pageable"
        json.totalPages == 1
        json.totalElements == 3
        json.content[0].location == "Portrush"
        json.content[1].location == "Cork"
        json.content[2].location == "Belfast"
        json.content.size == 3
    }

    def "LocationController - REST - Page2"() {
        given: "an entity"
        def loc1 = "Belfast"
        def loc2 = "Cork"
        def loc3 = "Portrush"
        def e1 = locationRepository.save(new LocationEntity(loc1))
        def e2 = locationRepository.save(new LocationEntity(loc2))
        def e3 = locationRepository.save(new LocationEntity(loc3))
        when: "a valid call to listPageable is made"
        def result = mockMvc.perform(get("/api/location/?page=0&size=1&sort=location,asc")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "result verifies the returned pageable"
        json.totalPages == 3
        json.totalElements == 3
        json.content[0].location == "Belfast"
        json.content.size == 1
    }

    def "LocationController - REST - AllLocations"() {
        given: "an entity"
        def loc1 = "Belfast"
        def loc2 = "Cork"
        def loc3 = "Portrush"
        def e1 = locationRepository.save(new LocationEntity(loc1))
        def e2 = locationRepository.save(new LocationEntity(loc2))
        def e3 = locationRepository.save(new LocationEntity(loc3))
        when: "a valid call to getLocations is made"
        def result = mockMvc.perform(get("/api/location/locations")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "result verifies the returned pageable"
        json[0].location == loc1
        json[1].location == loc2
        json[2].location == loc3
    }

    def "LocationController - REST - FullLocationEntity"() {
        given: "an entity"
        def loc1 = "Belfast"
        def e1 = locationRepository.save(new LocationEntity(loc1))
        def car_json = '{"name": "Test Car", "registration": "192-D-12345", "manufacturer": "Tesla", "model": "S", "transmission": "AUTOMATIC", "category": "ELECTRIC", "location": {"id": 1, "location": "Belfast"}}'
        mockMvc.perform(post("/api/car/").contentType(MediaType.APPLICATION_JSON).content(car_json))
        when: "a valid call to findById is made"
        def result = mockMvc.perform(get("/api/location/{id}", 1)).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "result is an entity with an id"
        json.id == 1
        json.location == loc1
        json.cars.id[0] == 1
        json.cars.registration[0] == "192-D-12345"
    }
}
