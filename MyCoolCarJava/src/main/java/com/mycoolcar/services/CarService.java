package com.mycoolcar.services;

import com.mycoolcar.dtos.CarDto;
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

    private final FileService fileService;


    @Autowired
    public CarService(CarRepository carRepository, FileService fileService) {
        this.carRepository = carRepository;
        this.fileService = fileService;
    }

    public List<CarDto> getAllCars() {
        log.info("Fetching all cars with a rate of 7 or higher");
        return carRepository.findAllByRateIsGreaterThanEqualOrderByRateAsc(7);
    }

    public Car addNewCar(User user, MultipartFile[] images, MultipartFile mainImage, String carBrand,
                         String carModel, Integer carProductYear, String carDescription) throws IOException {

        log.info("Adding a new car for user: {}", user.getUsername());
        Car newCar = new Car(user, carBrand, carModel, carProductYear, carDescription);
        if (!mainImage.isEmpty()) {
            log.info("Uploading main image for the new car");
            String mainImageUrl = fileService.uploadFile(mainImage);
            newCar.setMainImageUrl(mainImageUrl);
        }
        if (images.length > 0) {
            log.info("Uploading {} additional images for the new car", images.length);
            List<String> imagesUrls = new ArrayList<>();
            for (MultipartFile image : images) {
                imagesUrls.add(fileService.uploadFile(image));
            }
            newCar.setImagesUrl(imagesUrls);
        }

        Car savedCar = carRepository.save(newCar);
        log.info("New car added successfully for user: {}, with Car ID: {}", user.getUsername(), savedCar.getId());
        return savedCar;
    }

    public Optional<Car> editCar(Long carId,
                                 String carBrand, String carModel,
                                 Integer carProductYear, String carDescription) {
        Optional<Car> car = carRepository.findById(carId);
        if (car.isEmpty()) {
            log.error("Car with ID: {} not found", carId);
            throw new ResourceNotFoundException("Car with id: " + carId + "  is not found");
        }
        Car editedCar = car.get();

        editedCar.setBrand(carBrand);
        editedCar.setModel(carModel);
        editedCar.setProductYear(carProductYear);
        editedCar.setDescription(carDescription);
        log.info("Car with ID: {} edited successfully", carId);
        return Optional.of(carRepository.save(editedCar));
    }

    public void deleteCar(Long carId) throws IOException{
        log.info("Deleting car with ID: {}", carId);
        Optional<Car> car = carRepository.findById(carId);
        if (car.isEmpty()) {
            log.error("Car with ID: {} not found", carId);
            throw new ResourceNotFoundException("Car with id: " + carId + "  is not found");
        }
        Car deletedCar = car.get();
        List<String> imagesUrls = deletedCar.getImagesUrl();
        if (imagesUrls != null && !imagesUrls.isEmpty()) {
            log.info("Deleting {} images for car with ID: {}", imagesUrls.size(), carId);
            for (String imagesUrl : imagesUrls) {
                fileService.deleteFile(imagesUrl);
            }
        }
        String mainImage = deletedCar.getMainImageUrl();
        if (mainImage != null) {
            log.info("Deleting main image for car with ID: {}", carId);
            fileService.deleteFile(mainImage);
        }
        // user.removeCar(deletedCar);
        carRepository.delete(deletedCar);
        log.info("Car with ID: {} deleted successfully", carId);
    }
}
