package com.mycoolcar.repositories;

import com.mycoolcar.dtos.CarCreationDto;
import com.mycoolcar.entities.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<CarEntity,Long> {
     List<CarCreationDto> findAllByRateIsGreaterThanEqualOrderByRateAsc(int rate);
}
