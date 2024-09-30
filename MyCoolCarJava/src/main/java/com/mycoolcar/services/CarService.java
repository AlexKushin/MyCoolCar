package com.mycoolcar.services;

import com.mycoolcar.dtos.CarDto;
import com.mycoolcar.dtos.CarEditDto;
import com.mycoolcar.entities.Car;
import com.mycoolcar.entities.CarLogbook;
import com.mycoolcar.entities.User;
import com.mycoolcar.exceptions.ResourceNotFoundException;
import com.mycoolcar.repositories.CarRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

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
    @Value("${car.rate}")
    private int rate;


    @Autowired
    public CarService(CarRepository carRepository, FileService fileService) {
        this.carRepository = carRepository;
        this.fileService = fileService;
    }

    public List<CarDto> getAllCars() {
        log.info("Fetching all cars with a rate of {} or higher", rate);
        return carRepository.findAllByRateIsGreaterThanEqualOrderByRateAsc(rate);
    }

    public CarDto saveNewCar(User user, MultipartFile[] images, MultipartFile mainImage, String carBrand,
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
        CarLogbook carLogbook = new CarLogbook();

        newCar.setLogbook(carLogbook);
        carLogbook.setCar(newCar);
        Car savedCar = carRepository.save(newCar);
        log.info("New car added successfully for user: {}, with Car ID: {}", user.getUsername(), savedCar.getId());
        savedCar = fileService.generateCarImagesToPreassignedUrls(savedCar);
        return mapCarToDto(savedCar);
    }

    public CarDto editCar(Long carId, CarEditDto carEditDto) {
        Car carToEdit = getCarById(carId);
        carToEdit.setBrand(carEditDto.brand());
        carToEdit.setModel(carEditDto.model());
        carToEdit.setProductYear(carEditDto.productYear());
        carToEdit.setDescription(carEditDto.description());
        log.info("Car with ID: {} edited successfully", carId);
        Car editedCar = carRepository.save(carToEdit);
        editedCar = fileService.generateCarImagesToPreassignedUrls(editedCar);

        return mapCarToDto(editedCar);
    }

    public List<Car> getUserCars(User user) {
        List<Car> userCars = user.getUserCars();
        userCars.replaceAll(fileService::generateCarImagesToPreassignedUrls);
        return userCars;
    }

    public List<Car> getUserSubscribedCars(User user) {
        List<Car> userSubscribedCars = user.getSubscribedCars();
        userSubscribedCars.replaceAll(fileService::generateCarImagesToPreassignedUrls);
        return userSubscribedCars;
    }

    public void deleteCar(Long carId) throws IOException {
        log.info("Deleting car with ID: {}", carId);
        Car deletedCar = getCarById(carId);
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
        carRepository.delete(deletedCar);
        log.info("Car with ID: {} deleted successfully", carId);
    }

    private Car getCarById(long carId) {
        Optional<Car> car = carRepository.findById(carId);
        if (car.isEmpty()) {
            log.error("Car with ID: {} not found", carId);
            throw new ResourceNotFoundException("Car with id: " + carId + "  is not found");
        }
        return car.get();
    }

    private CarDto mapCarToDto(Car car) {
        return new CarDto(car.getId(), car.getBrand(),
                car.getModel(), car.getProductYear(),
                car.getDescription(), car.getMainImageUrl(),
                car.getImagesUrl(), car.getRate(),
                car.getUser().getId(), car.getUser().getFirstName());
    }
}
