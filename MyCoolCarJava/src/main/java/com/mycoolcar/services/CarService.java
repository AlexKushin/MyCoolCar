package com.mycoolcar.services;

import com.mycoolcar.dtos.CarCreationDto;
import com.mycoolcar.entities.CarEntity;
import com.mycoolcar.entities.User;
import com.mycoolcar.repositories.CarRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CarService {

    private final CarRepository carRepository;


    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;

    }

    public List<CarCreationDto> getAllCars (){
       // return carRepository.findAllByRateIsGreaterThanEqual(7);
        return carRepository.findAllByRateIsGreaterThanEqualOrderByRateAsc(7);
    }

    public Optional<CarEntity> createCar(Optional<User> user, CarCreationDto carCreationDto) {
        //todo https://www.baeldung.com/java-optional
        //todo https://stackoverflow.com/questions/31696485/why-use-optional-of-over-optional-ofnullable
        log.debug("car dto {}", carCreationDto);
        return user.isPresent() ? Optional.ofNullable
                (carRepository.save(new CarEntity(carCreationDto.brand(),
                        carCreationDto.model(),
                        carCreationDto.productYear(),
                        carCreationDto.description(),
                        carCreationDto.image()))) : Optional.empty();
    }

}
