package com.mycoolcar.repositories;

import com.mycoolcar.entities.Car;
import com.mycoolcar.entities.CarLogbook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarLogbookRepository extends JpaRepository <CarLogbook, Long> {

     List<CarLogbook> findAllByCarIn(List<Car> subscribedCars);

     Optional <CarLogbook> findByCar_Id (Long carId);
}
