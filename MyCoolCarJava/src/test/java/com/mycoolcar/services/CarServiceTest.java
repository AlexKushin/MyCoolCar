package com.mycoolcar.services;

import com.mycoolcar.dtos.CarDto;
import com.mycoolcar.dtos.CarEditDto;
import com.mycoolcar.entities.Car;
import com.mycoolcar.entities.CarLogbook;
import com.mycoolcar.entities.User;
import com.mycoolcar.exceptions.ResourceNotFoundException;
import com.mycoolcar.repositories.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarServiceTest  {

    @Mock
    private CarRepository carRepository;

    @Mock
    private FileService fileService;

    @Mock
    private MultipartFile mockMainImage;

    @Mock
    private MultipartFile mockAdditionalImage;

    @InjectMocks
    private CarService carService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(); // Initialize a dummy user object for testing
        user.setFirstName("testuser");
        user.setEmail("testuser@mycoolcar.com");
    }
    @Test
    void getAllCars_ShouldReturnCarList_WhenRateIsValid() {
        // Arrange
        CarDto car1 = new CarDto(1L, "Brand1", "Model1", 2020, "Description",
                "mainImageUrl",Arrays.asList("img1", "img2"),8,1L,"testuser");
        CarDto car2 = new CarDto(2L, "Brand1", "Model1", 2020, "Description",
                "mainImageUrl",Arrays.asList("img1", "img2"),8,2L,"testuser");

        when(carRepository.findAllByRateIsGreaterThanEqualOrderByRateAsc(anyInt())).thenReturn(Arrays.asList(car1, car2));

        // Act
        List<CarDto> result = carService.getAllCars();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(carRepository, times(1)).findAllByRateIsGreaterThanEqualOrderByRateAsc(anyInt());
    }

    @Test
    void saveNewCar_ShouldSaveNewCar_WithImages() throws IOException {
        // Arrange
        MultipartFile[] images = {mockAdditionalImage};
        when(mockMainImage.isEmpty()).thenReturn(false);
        when(fileService.uploadFile(mockMainImage)).thenReturn("mainImageUrl");
        when(fileService.uploadFile(mockAdditionalImage)).thenReturn("imageUrl");

        Car car = new Car(1L, "Brand", "Model", 2020,
                "Description","mainImageUrl",Arrays.asList("img1", "img2"), 8,
                new CarLogbook(),user);
        when(carRepository.save(any(Car.class))).thenReturn(car);
        when(fileService.generateCarImagesToPreassignedUrls(any(Car.class))).thenReturn(car);

        // Act
        CarDto result = carService.saveNewCar(user, images, mockMainImage,
                "Brand", "Model", 2020, "Description");

        // Assert
        assertNotNull(result);
        assertEquals("Brand", result.brand());
        verify(carRepository, times(1)).save(any(Car.class));
        verify(fileService, times(1)).uploadFile(mockMainImage);
        verify(fileService, times(1)).uploadFile(mockAdditionalImage);
    }

    @Test
    void saveNewCar_ShouldSaveNewCar_WithoutImages() throws IOException {
        // Arrange
        MultipartFile[] images = {};
        when(mockMainImage.isEmpty()).thenReturn(true);

        Car car = new Car(1L, "Brand", "Model", 2020,
                "Description",null,null, 8,
                new CarLogbook(),user);
        when(carRepository.save(any(Car.class))).thenReturn(car);
        when(fileService.generateCarImagesToPreassignedUrls(any(Car.class))).thenReturn(car);


        // Act
        CarDto result = carService.saveNewCar(user, images, mockMainImage, "Brand", "Model", 2020, "Description");

        // Assert
        assertNotNull(result);
        assertEquals("Brand", result.brand());
        verify(carRepository, times(1)).save(any(Car.class));
        verify(fileService, never()).uploadFile(any(MultipartFile.class)); // No images uploaded
    }

    @Test
    void editCar_ShouldEditCarDetailsSuccessfully() {
        // Arrange
        Long carId = 1L;
        CarEditDto carEditDto = new CarEditDto("NewBrand", "NewModel", 2021, "NewDescription");
        Car existingCar = new Car(1L, "OldBrand", "OldModel", 2020,
                "OldDescription","mainImageUrl",Arrays.asList("img1", "img2"), 8,
                new CarLogbook(),user);
        when(carRepository.findById(carId)).thenReturn(Optional.of(existingCar));
        when(carRepository.save(existingCar)).thenReturn(existingCar);
        when(fileService.generateCarImagesToPreassignedUrls(existingCar)).thenReturn(existingCar);


        // Act
        CarDto result = carService.editCar(carId, carEditDto);

        // Assert
        assertNotNull(result);
        assertEquals("NewBrand", result.brand());
        assertEquals("NewModel", result.model());
        verify(carRepository, times(1)).findById(carId);
        verify(carRepository, times(1)).save(existingCar);
    }

    @Test
    void deleteCar_ShouldDeleteCarAndImagesSuccessfully() throws IOException {
        // Arrange
        Long carId = 1L;
        Car car = new Car(user, "Brand", "Model", 2020, "Description");
        car.setImagesUrl(Arrays.asList("image1", "image2"));
        car.setMainImageUrl("mainImage");
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        // Act
        carService.deleteCar(carId);

        // Assert
        verify(fileService, times(1)).deleteFile("image1");
        verify(fileService, times(1)).deleteFile("image2");
        verify(fileService, times(1)).deleteFile("mainImage");
        verify(carRepository, times(1)).delete(car);
    }

    @Test
    void getCarById_ShouldThrowException_WhenCarNotFound() {
        // Arrange
        Long carId = 1L;
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            carService.deleteCar(carId);
        });

        verify(carRepository, times(1)).findById(carId);
    }

}