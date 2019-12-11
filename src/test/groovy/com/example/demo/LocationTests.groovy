package com.example.demo

import com.example.demo.entities.LocationEntity
import com.example.demo.repositories.LocationRepository
import com.example.demo.services.LocationService
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

    def "LocationService - tests"() {
        given: "an entity"
        def id1 = 1
        def loc1 = "Belfast"
        when: "createLocation service is called"
        def e1 = locationService.createLocation(loc1)
        then: "LocationEntity matches location"
        e1.getLocation() == loc1

        when: "findById service is called with a valid id"
        def result = locationService.findById(id1)
        then: "result is an entity with an id"
        result.get().id == id1
        result.get().id > 0

        def id2 = 2
        when: "findById service is called with an invalid id"
        def invalidResult = locationService.findById(id2)
        then: "result is an empty optional"
        invalidResult == Optional.empty()

        def loc2 = "Cork"
        def e2 = locationService.createLocation(loc2)
        when: "getAllLocations service is called"
        def allLocsResult = locationService.getAllLocations()
        then: "check results are valid"
        !allLocsResult.isEmpty()
        and: "includes both added locations"
        id1 in allLocsResult[0].id
        id2 in allLocsResult[1].id

        def loc3 = "Portrush"
        def e3 = locationService.createLocation(loc3)
        def pageNo = 0
        def pageSize = 3
        def totalPages = 1
        def totalElements = 3
        Pageable testPage = PageRequest.of(pageNo, pageSize, Sort.by("location").descending())
        when: "findAll(Pageable) service is called"
        def pageableResult = locationService.findAll(testPage)
        then: "pageable result should be valid"
        pageableResult.totalElements == totalElements
        pageableResult.totalPages == totalPages
        pageableResult.content[0].getLocation() == loc3
        pageableResult.content[1].getLocation() == loc2
        pageableResult.content[2].getLocation() == loc1

    }

    def "LocationController - REST - adding valid location test"() {
        given: "an entity"
        when: "a valid call to addLocation is made"
        def result = mockMvc.perform(put("/api/location/addLocation?location=Cork")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "result is an entity with an id"
        json.id == 1
        json.id > 0
    }

    def "LocationController - REST - adding invalid location test"() {
        given: "an entity"
        def e1 = locationRepository.save(new LocationEntity("Cork"))
        when: "an invalid call to addLocation is made"
        def except = mockMvc.perform(put("/api/location/addLocation?location=Cork")).andReturn().getResolvedException().getClass()
        then: "response status exception thrown"
        assert(except.is(ResponseStatusException))
    }

    def "LocationController - REST - valid findById"() {
        given: "an entity"
        def loc = "Cork"
        def e1 = locationRepository.save(new LocationEntity(loc))
        when: "a valid call to addLocation is made"
        def result = mockMvc.perform(get("/api/location/findById?id=1")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "result is an entity with an id"
        json.id == 1
        json.location == loc
    }

    def "LocationController - REST - invalid findById"() {
        given: "an entity"
        def loc = "Cork"
        def e1 = locationRepository.save(new LocationEntity(loc))
        when: "a valid call to addLocation is made"
        def except = mockMvc.perform(get("/api/location/findById?id=2")).andReturn().getResolvedException().getClass()
        then: "response status exception thrown"
        assert(except.is(ResponseStatusException))
    }

    def "LocationController - REST - valid deleteById"() {
        given: "an entity"
        def loc = "Cork"
        def e1 = locationRepository.save(new LocationEntity(loc))
        when: "a valid call to deleteById is made"
        def result = mockMvc.perform(delete("/api/location/deleteById?id=1")).andReturn().response.contentAsString
        then: "result verifies the delete"
        result == "location deleted successfully."
    }

    def "LocationController - REST - invalid deleteById"() {
        given: "an entity"
        def loc = "Cork"
        def e1 = locationRepository.save(new LocationEntity(loc))
        when: "an invalid call to deleteById is made"
        def except = mockMvc.perform(delete("/api/location/deleteById?id=2")).andReturn().getResolvedException().getClass()
        then: "response status exception thrown"
        assert(except.is(ResponseStatusException))
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
        def result = mockMvc.perform(get("/api/location/listPageable?page=0&size=3&sort=location,desc")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "result verifies the returned pageable"
        json.totalPages == 1
        json.totalElements == 3
        json.content[0].location == "Portrush"
        json.content[1].location == "Cork"
        json.content[2].location == "Belfast"
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
        def result = mockMvc.perform(get("/api/location/listPageable?page=0&size=1&sort=location,asc")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "result verifies the returned pageable"
        json.totalPages == 3
        json.totalElements == 3
        json.content[0].location == "Belfast"
    }
}
