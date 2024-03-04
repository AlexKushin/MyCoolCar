package com.mycoolcar.repositories;

import com.mycoolcar.entities.CarLogPost;
import com.mycoolcar.entities.CarLogbook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarLogPostRepository extends JpaRepository<CarLogPost, Long> {

    List<CarLogPost> findAllByCarLogbookInOrderByCreatedTime(List<CarLogbook> logbooks);
}
