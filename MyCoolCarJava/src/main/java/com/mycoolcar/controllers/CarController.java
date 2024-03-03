package com.mycoolcar.controllers;

import com.mycoolcar.dtos.CarCreationDto;
import com.mycoolcar.entities.Car;
import com.mycoolcar.entities.User;
import com.mycoolcar.services.CarService;
import com.mycoolcar.services.GoogleFileServiceImpl;
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
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/")
@CrossOrigin(origins = "http://localhost:4200")
public class CarController {

    private final CarService carService;

    private final UserService userService;

    private final GoogleFileServiceImpl fileService;

    @Autowired
    public CarController(CarService carService, UserService userService, GoogleFileServiceImpl fileService) {
        this.carService = carService;
        this.userService = userService;
        this.fileService = fileService;
    }

    @GetMapping("cars")
    public List<CarCreationDto> getAllCars() {
        return carService.getAllCars();
    }

    @PostMapping("cars")
    public ResponseEntity<Car> postCar(Principal principal,
                                       @RequestPart("files[]") MultipartFile[] images,
                                       @RequestPart("mainImage") MultipartFile mainImage,
                                       @RequestParam("brand") String carBrand,
                                       @RequestParam("model") String carModel,
                                       @RequestParam("productYear") Integer carProductYear,
                                       @RequestParam("description") String carDescription) throws IOException {

        Optional<User> userOptional = userService.getByUsername(principal.getName());

        Car newCar = new Car(carBrand, carModel,
                carProductYear, carDescription);
        if(!mainImage.isEmpty()){
            String mainImageUrl = fileService.uploadFile(mainImage);
            newCar.setMainImageUrl(mainImageUrl);
        }
        if(images.length > 0){
            String[] imagesUrls = new String[8];
            for (int i = 0; i < images.length; i++) {
                imagesUrls[i] = fileService.uploadFile(images[i]);
            }
            newCar.setImagesUrl(imagesUrls);
        }
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.addCar(newCar);
            userService.save(user);
        }
        return userOptional.isEmpty() ? new ResponseEntity<>(HttpStatus.CONFLICT)
                : new ResponseEntity<>(newCar, HttpStatus.CREATED);

    }
}
