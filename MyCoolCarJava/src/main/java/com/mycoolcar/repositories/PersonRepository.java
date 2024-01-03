package com.mycoolcar.repositories;

import com.mycoolcar.entities.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Long> {

    Optional<PersonEntity> findByEmail(String email);

    Optional<PersonEntity> findAllByName(String name);
}
