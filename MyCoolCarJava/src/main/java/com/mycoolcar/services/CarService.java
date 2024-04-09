package com.mycoolcar.services;

import com.mycoolcar.dtos.CarCreationDto;
import com.mycoolcar.entities.Car;
import com.mycoolcar.entities.User;
import com.mycoolcar.exceptions.ResourceNotFoundException;
import com.mycoolcar.repositories.CarRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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
            List <String> imagesUrls = new ArrayList<>();
            for (MultipartFile image : images) {
                imagesUrls.add(fileService.uploadFile(image));
            }
            newCar.setImagesUrl(imagesUrls);
        }
        user.addCar(newCar);
        userService.save(user);
        return newCar;
    }

    public Optional<Car> editCar(Long carId, MultipartFile[] images,
                                 MultipartFile mainImage, List<String> deletedImages,
                                 String carBrand, String carModel,
                                 Integer carProductYear, String carDescription) throws IOException {
        Optional<Car> car = carRepository.findById(carId);
        if (car.isEmpty()) {
            throw new ResourceNotFoundException("Car with id: " + carId + "  is not found");
        }
        Car editedCar = car.get();
        if (!mainImage.isEmpty()) {
            fileService.deleteFile(editedCar.getMainImageUrl());
            String mainImageUrl = fileService.uploadFile(mainImage);
            editedCar.setMainImageUrl(mainImageUrl);
        }
        deletedImages.forEach(fileService::deleteFile);
        if (images.length > 0) {
            List <String> imagesUrls = editedCar.getImagesUrl();
            if (imagesUrls == null) {
                imagesUrls = new ArrayList<>();
            }
            for (MultipartFile image : images) {
                imagesUrls.add(fileService.uploadFile(image));
            }
            editedCar.setImagesUrl(imagesUrls);
        }
        editedCar.setBrand(carBrand);
        editedCar.setModel(carModel);
        editedCar.setProductYear(carProductYear);
        editedCar.setDescription(carDescription);
        return Optional.of(carRepository.save(editedCar));
    }

    public void deleteCar(User user, Long carId) {
        Optional<Car> car = carRepository.findById(carId);
        if(car.isEmpty()){
            throw new ResourceNotFoundException("Car with id: " + carId + "  is not found");
        }
        Car deletedCar = car.get();
        List <String> imagesUrls = deletedCar.getImagesUrl();
        if(imagesUrls != null && !imagesUrls.isEmpty() ){
            imagesUrls.forEach(fileService::deleteFile);
        }
        String mainImage = deletedCar.getMainImageUrl();
        if(mainImage != null ){
            fileService.deleteFile(mainImage);
        }
        user.removeCar(deletedCar);
        carRepository.delete(deletedCar);
    }
}
