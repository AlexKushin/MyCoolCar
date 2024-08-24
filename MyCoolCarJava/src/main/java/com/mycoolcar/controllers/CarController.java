package com.mycoolcar.controllers;

import com.mycoolcar.dtos.CarDto;
import com.mycoolcar.dtos.CarEditDto;
import com.mycoolcar.entities.Car;
import com.mycoolcar.entities.User;
import com.mycoolcar.services.AwsS3ServiceImpl;
import com.mycoolcar.services.CarService;
import com.mycoolcar.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/")
public class CarController {

    private final CarService carService;

    private final UserService userService;

    private final AwsS3ServiceImpl fileService;


    @Autowired
    public CarController(CarService carService, UserService userService, AwsS3ServiceImpl fileService) {
        this.carService = carService;
        this.userService = userService;
        this.fileService = fileService;
    }

    @GetMapping("top_cars")
    public List<CarDto> getAllCars() {
        return carService.getAllCars();
    }

    @PostMapping("cars/new")
    public ResponseEntity<CarDto> postCar(Principal principal,
                                          @RequestPart("files[]") MultipartFile[] images,
                                          @RequestPart("mainImage") MultipartFile mainImage,
                                          @RequestParam("brand") String carBrand,
                                          @RequestParam("model") String carModel,
                                          @RequestParam("productYear") Integer carProductYear,
                                          @RequestParam("description") String carDescription) throws IOException {

        Optional<User> userOptional = userService.getUserByEmail(principal.getName());
        Car newCar = new Car();
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            newCar = carService.addNewCar(user, images, mainImage, carBrand,
                    carModel, carProductYear, carDescription);
        }
        newCar = fileService.generateCarImagesToPreassignedUrls(newCar);
        return userOptional.isEmpty() ? new ResponseEntity<>(HttpStatus.CONFLICT)
                : new ResponseEntity<>(new CarDto(newCar.getId(), newCar.getBrand(), newCar.getModel(),
                newCar.getProductYear(), newCar.getDescription(), newCar.getMainImageUrl(),
                newCar.getImagesUrl(), newCar.getRate(), newCar.getUser().getId(),
                newCar.getUser().getFirstName()), HttpStatus.CREATED);
    }

    @PutMapping("cars/{carId}")
    public ResponseEntity<CarDto> editCar(@PathVariable Long carId,
                                          @RequestBody CarEditDto car)  {

        Optional<Car> editedCar = carService.editCar(
                carId, car.getBrand(), car.getModel(), car.getProductYear(), car.getDescription());
        if (editedCar.isPresent()) {
            Car editedCarRes = fileService.generateCarImagesToPreassignedUrls(editedCar.get());

            log.info("editedCarRes {}", editedCarRes.toString());
            return new ResponseEntity<>(new CarDto(editedCarRes.getId(), editedCarRes.getBrand(), editedCarRes.getModel(),
                    editedCarRes.getProductYear(), editedCarRes.getDescription(), editedCarRes.getMainImageUrl(),
                    editedCarRes.getImagesUrl(), editedCarRes.getRate(), editedCarRes.getUser().getId(),
                    editedCarRes.getUser().getFirstName()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("cars/{carId}")
    public ResponseEntity<Car> deleteCar(Principal principal, @PathVariable Long carId)  {
        Optional<User> user = userService.getUserByEmail(principal.getName());
        user.ifPresent(value -> {
            try {
                carService.deleteCar(carId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return user.isEmpty() ? new ResponseEntity<>(HttpStatus.BAD_REQUEST)
                : new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("cars")
    public ResponseEntity<List<Car>> getUserCars(Principal principal) {

        Optional<User> userOptional = userService.getUserByEmail(principal.getName());

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("user was not found");
        }
        List<Car> userCars = userOptional.get().getUserCars();

        userCars.replaceAll(fileService::generateCarImagesToPreassignedUrls);

        return new ResponseEntity<>(userCars, HttpStatus.OK);
    }

    @GetMapping("subscribed_cars")
    public ResponseEntity<List<Car>> getUserSubscribedCars(Principal principal) {

        Optional<User> userOptional = userService.getUserByEmail(principal.getName());

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("user was not found");
        }
        List<Car> userSubscribedCars = userOptional.get().getSubscribedCars();

        return new ResponseEntity<>(userSubscribedCars, HttpStatus.OK);
    }
}
