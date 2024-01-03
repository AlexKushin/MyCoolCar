package com.mycoolcar.repositories;

import com.mycoolcar.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

   // Optional <User> findByUsername(String username);
    Optional<User> findById(Long id);

    Optional<User> findAllByFirstName(String name);
}