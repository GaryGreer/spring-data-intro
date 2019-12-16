package com.example.demo

import com.example.demo.entities.CarEntity
import com.example.demo.entities.LocationEntity
import com.example.demo.enums.Category
import com.example.demo.enums.Transmission
import com.example.demo.repositories.CustomerRepository
import com.example.demo.repositories.LocationRepository
import com.example.demo.services.CarService
import com.example.demo.services.LocationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

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

    def "CarService - tests"() {
        given: "an entity"
        def name = "Test Car"
        def reg = "12345"
        def manufacturer = "Ford"
        def model = "Fiesta"
        def transmission = Transmission.MANUAL
        def category = Category.DIESEL
        def loc_id = 1
        def location = new LocationEntity("Test")
        when: "createCar service is called"
        def c1 = carService.createCar(name, reg, manufacturer, model, transmission, category, location)
        then: "customerEntity matches name"
        c1.getName() == name
        c1.getRegistration() == reg
        c1.getManufacturer() == manufacturer
        c1.getModel() == model
        c1.getTransmission() == transmission
        c1.getCategory() == category
        c1.getLocation() == location
        c1.getLocation().getId() == loc_id

        when: "findById service is called with a valid id"
        def id1 = 1
        def result = carService.findById(id1)
        then: "result is an entity with an id"
        result.get().id == id1
        result.get().id > 0
    }

}
