package com.mycoolcar.services;

import com.mycoolcar.dtos.CarDto;
import com.mycoolcar.entities.Car;
import com.mycoolcar.entities.User;
import com.mycoolcar.repositories.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarServiceTest  {

    @Mock
    private CarRepository carRepository;

    @Mock
    private GoogleFileServiceImpl fileService;

    @Mock
    private UserService userService;
    @InjectMocks
    private CarService carService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCars() {
        // Mocking repository behavior
        when(carRepository.findAllByRateIsGreaterThanEqualOrderByRateAsc(anyInt())).thenReturn(new ArrayList<>());

        List<CarDto> result = carService.getAllCars();

        assertEquals(0, result.size());
    }

    @Test
    void testAddNewCar() throws IOException {
        // Prepare test data
        User user = new User();
        MultipartFile[] images = new MultipartFile[0];
        MultipartFile mainImage = mock(MultipartFile.class);
        String carBrand = "Brand";
        String carModel = "Model";
        Integer carProductYear = 2020;
        String carDescription = "Description";

        // Mocking file service behavior
        when(fileService.uploadFile(any(MultipartFile.class))).thenReturn("mockedUrl");

        // Execute the method under test
        Car result = carService.addNewCar(user, images, mainImage, carBrand, carModel, carProductYear, carDescription);

        // Verify interactions and assertions
        assertEquals(carBrand, result.getBrand());
        assertEquals(carModel, result.getModel());
        assertEquals(carProductYear, result.getProductYear());
        assertEquals(carDescription, result.getDescription());
        assertEquals("mockedUrl", result.getMainImageUrl());
        verify(userService, times(1)).save(user);
    }

}