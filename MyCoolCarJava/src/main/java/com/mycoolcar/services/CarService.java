package com.mycoolcar.services;

import com.mycoolcar.dtos.CarCreationDto;
import com.mycoolcar.entities.Car;
import com.mycoolcar.entities.User;
import com.mycoolcar.repositories.CarRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Slf4j
@Service
public class CarService {

    private final CarRepository carRepository;

    private final GoogleFileServiceImpl fileService;

    private final UserService userService;

    @Autowired
    public CarService(CarRepository carRepository, GoogleFileServiceImpl fileService, UserService userService) {
        this.carRepository = carRepository;
        this.fileService = fileService;
        this.userService = userService;
    }

    public List<CarCreationDto> getAllCars() {
        return carRepository.findAllByRateIsGreaterThanEqualOrderByRateAsc(7);
    }

    public Car addNewCar(User user, MultipartFile[] images, MultipartFile mainImage, String carBrand,
                         String carModel, Integer carProductYear, String carDescription) throws IOException {

        Car newCar = new Car(carBrand, carModel,
                carProductYear, carDescription);
        if (!mainImage.isEmpty()) {
            String mainImageUrl = fileService.uploadFile(mainImage);
            newCar.setMainImageUrl(mainImageUrl);
        }
        if (images.length > 0) {
            String[] imagesUrls = new String[8];
            for (int i = 0; i < images.length; i++) {
                imagesUrls[i] = fileService.uploadFile(images[i]);
            }
            newCar.setImagesUrl(imagesUrls);
        }
        user.addCar(newCar);
        userService.save(user);
        return newCar;
    }

}
