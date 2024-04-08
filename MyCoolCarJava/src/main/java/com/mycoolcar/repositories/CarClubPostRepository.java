package com.mycoolcar.repositories;

import com.mycoolcar.entities.CarClub;
import com.mycoolcar.entities.ClubPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarClubPostRepository extends JpaRepository<ClubPost, Long> {

   List<ClubPost> findAllByCarClubInOrderByCreatedTime(List<CarClub> subscribedCarClubs);
}