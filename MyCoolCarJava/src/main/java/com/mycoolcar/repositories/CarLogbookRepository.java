package com.mycoolcar.repositories;

import com.mycoolcar.entities.CarEntity;
import com.mycoolcar.entities.CarLogbook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarLogbookRepository extends JpaRepository <CarLogbook, Long> {

     Optional <CarLogbook> findById (Long id);

     List<CarLogbook> findAllByCarIn(List<CarEntity> subscribedCars);
}
