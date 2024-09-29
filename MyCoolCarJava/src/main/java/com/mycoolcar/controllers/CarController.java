package com.mycoolcar.controllers;

import com.mycoolcar.dtos.CarDto;
import com.mycoolcar.dtos.CarEditDto;
import com.mycoolcar.entities.Car;
import com.mycoolcar.entities.User;
import com.mycoolcar.services.CarService;
import com.mycoolcar.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/")
public class CarController {

    private final CarService carService;

    private final UserService userService;

    @Autowired
    public CarController(CarService carService, UserService userService) {
        this.carService = carService;
        this.userService = userService;
    }

    @GetMapping("top_cars")
    public ResponseEntity<List<CarDto>> getAllCars() {
        return new ResponseEntity<>(carService.getAllCars(), HttpStatus.OK);
    }

    @PostMapping("cars/new")
    public ResponseEntity<CarDto> postCar(Principal principal,
                                          @RequestPart("files[]") MultipartFile[] images,
                                          @RequestPart("mainImage") MultipartFile mainImage,
                                          @RequestParam("brand") String carBrand,
                                          @RequestParam("model") String carModel,
                                          @RequestParam("productYear") Integer carProductYear,
                                          @RequestParam("description") String carDescription) throws IOException {

        User user = userService.getUserByEmail(principal.getName());

        CarDto newCar = carService.saveNewCar(user, images, mainImage, carBrand,
                carModel, carProductYear, carDescription);
        return new ResponseEntity<>(newCar, HttpStatus.CREATED);
    }

    @PutMapping("cars/{carId}")
    public ResponseEntity<CarDto> editCar(@PathVariable Long carId, @RequestBody CarEditDto car) {
        CarDto editedCar = carService.editCar(carId, car);
        log.info("editedCarRes {}", editedCar.toString());
        return new ResponseEntity<>(editedCar, HttpStatus.OK);
    }

    @DeleteMapping("cars/{carId}")
    public ResponseEntity<Car> deleteCar(@PathVariable Long carId) {
        try {
            carService.deleteCar(carId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("cars/my")
    public ResponseEntity<List<Car>> getUserCars(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        List<Car> userCars = carService.getUserCars(user);
        return new ResponseEntity<>(userCars, HttpStatus.OK);
    }

    @GetMapping("cars/subscriptions")
    public ResponseEntity<List<Car>> getUserSubscribedCars(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        List<Car> userSubscribedCars = carService.getUserSubscribedCars(user);
        return new ResponseEntity<>(userSubscribedCars, HttpStatus.OK);
    }
}
