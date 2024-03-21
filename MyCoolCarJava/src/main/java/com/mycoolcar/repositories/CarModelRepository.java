package com.mycoolcar.repositories;

import com.mycoolcar.entities.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarModelRepository extends JpaRepository<CarModel, Integer> {
}
