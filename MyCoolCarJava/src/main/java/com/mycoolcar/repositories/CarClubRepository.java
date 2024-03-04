package com.mycoolcar.repositories;

import com.mycoolcar.entities.CarClub;
import com.mycoolcar.entities.CarLogbook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarClubRepository extends JpaRepository<CarClub, Long> {
    Optional<CarClub> findById (Long id);
}
