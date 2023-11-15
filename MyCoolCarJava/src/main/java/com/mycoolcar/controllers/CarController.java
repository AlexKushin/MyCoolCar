package com.mycoolcar.controllers;

import com.mycoolcar.dtos.CarCreationDto;
import com.mycoolcar.entities.CarEntity;
import com.mycoolcar.entities.PersonEntity;
import com.mycoolcar.services.CarService;
import com.mycoolcar.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("api/")
public class CarController {

    private final CarService carService;

    private final PersonService personService;

    @Autowired
    public CarController(CarService carService, PersonService personService) {
        this.carService = carService;
        this.personService = personService;
    }

    @PostMapping("cars")
    public ResponseEntity<CarEntity> postCar(Principal principal, @RequestBody CarCreationDto carCreationDto) {
        Optional<PersonEntity> person = personService.get(principal.getName());
        Optional<CarEntity> result = carService.createCar(person,carCreationDto);
        return result.isEmpty() ? new ResponseEntity<>(HttpStatus.CONFLICT)
                : new ResponseEntity<>(result.get(), HttpStatus.CREATED);
    }
}
