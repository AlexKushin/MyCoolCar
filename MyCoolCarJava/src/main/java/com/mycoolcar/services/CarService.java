package com.mycoolcar.services;

import com.mycoolcar.dtos.CarCreationDto;
import com.mycoolcar.entities.CarEntity;
import com.mycoolcar.entities.PersonEntity;
import com.mycoolcar.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CarService {

    private final CarRepository carRepository;


    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;

    }

    public Optional<CarEntity> createCar(Optional<PersonEntity> personEntity, CarCreationDto carCreationDto) {
        //todo https://www.baeldung.com/java-optional
        //todo https://stackoverflow.com/questions/31696485/why-use-optional-of-over-optional-ofnullable
        return personEntity.isPresent() ? Optional.ofNullable
                (carRepository.save(new CarEntity(carCreationDto.brand(),
                        carCreationDto.model(),
                        carCreationDto.productYear(),
                        carCreationDto.description(), personEntity.get()))) : Optional.empty();
    }

}
