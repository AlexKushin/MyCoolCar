package com.mycoolcar.services;

import com.mycoolcar.dtos.CarCreationDto;
import com.mycoolcar.repositories.CarRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class CarService {

    private final CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;

    }
    public List<CarCreationDto> getAllCars (){
        return carRepository.findAllByRateIsGreaterThanEqualOrderByRateAsc(7);
    }

}
