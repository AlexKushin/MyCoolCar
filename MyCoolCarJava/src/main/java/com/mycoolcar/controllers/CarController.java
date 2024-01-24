package com.mycoolcar.controllers;

import com.mycoolcar.dtos.CarCreationDto;
import com.mycoolcar.entities.CarEntity;
import com.mycoolcar.entities.User;
import com.mycoolcar.services.CarService;
import com.mycoolcar.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/")
@CrossOrigin(origins = "http://localhost:4200")
public class CarController {

    private final CarService carService;

    private final UserService userService;

    @Autowired
    public CarController(CarService carService, UserService userService) {
        this.carService = carService;
        this.userService = userService;
    }

    @GetMapping("cars")
    public List<CarCreationDto> getAllCars() {
        return carService.getAllCars();
    }
    @PostMapping("cars")
    public ResponseEntity<CarEntity> postCar(Principal principal, @RequestBody CarCreationDto carCreationDto) {
        Optional<User> userOptional = userService.getByUsername(principal.getName());
        CarEntity newCar = new CarEntity(carCreationDto.brand(), carCreationDto.model(),
                carCreationDto.productYear(), carCreationDto.description(), carCreationDto.image());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.addCar(newCar);
            userService.save(user);
        }
        return userOptional.isEmpty() ? new ResponseEntity<>(HttpStatus.CONFLICT)
                : new ResponseEntity<>(newCar, HttpStatus.CREATED);

    }
}
