package com.mycoolcar.repositories;

import com.mycoolcar.entities.CarBrand;
import com.mycoolcar.entities.CarClub;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarBrandRepository extends JpaRepository<CarBrand, Integer> {
}
