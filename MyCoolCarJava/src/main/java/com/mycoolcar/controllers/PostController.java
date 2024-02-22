package com.mycoolcar.controllers;

import com.mycoolcar.dtos.CarLogPostDto;
import com.mycoolcar.dtos.ClubPostDto;
import com.mycoolcar.entities.*;
import com.mycoolcar.repositories.CarClubRepository;
import com.mycoolcar.repositories.CarLogbookRepository;
import com.mycoolcar.services.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/")
@CrossOrigin(origins = "http://localhost:4200")
public class PostController {

    private final PostService postService;
    private final CarLogbookRepository carLogbookRepository;
    private final CarClubRepository carClubRepository;

    @Autowired
    public PostController(PostService postService, CarLogbookRepository carLogbookRepository,
                          CarClubRepository carClubRepository) {
        this.postService = postService;
        this.carLogbookRepository = carLogbookRepository;
        this.carClubRepository = carClubRepository;
    }

    @PostMapping("car-log-posts/new")
    public ResponseEntity<CarLogPost> postCarLog(@RequestBody CarLogPostDto carLogPostDto) {
        Optional<CarLogbook> carLogbook = carLogbookRepository.findById(carLogPostDto.logbookId());
        CarLogPost newCarLogPost = new CarLogPost();
        if (carLogbook.isPresent()) {
            newCarLogPost.setTopic(carLogPostDto.topic());
            newCarLogPost.setDescription(carLogPostDto.description());
            newCarLogPost.setCarLogbook(carLogbook.get());
            newCarLogPost.setCreatedTime(LocalDateTime.now());
            postService.post(newCarLogPost);
        }
        return carLogbook.isEmpty() ? new ResponseEntity<>(HttpStatus.CONFLICT)
                : new ResponseEntity<>(newCarLogPost, HttpStatus.CREATED);

    }

    @PostMapping("club-posts/new")
    public ResponseEntity<ClubPost> postClubPost(@RequestBody ClubPostDto clubPostDto) {
        Optional<CarClub> carClub = carClubRepository.findById(clubPostDto.carClubId());
        ClubPost newClubPost = new ClubPost();
        if(carClub.isPresent()){
            newClubPost.setCarClub(carClub.get());
            newClubPost.setTopic(clubPostDto.clubPostTopic());
            newClubPost.setDescription(clubPostDto.description());
            newClubPost.setCreatedTime(LocalDateTime.now());
            postService.post(newClubPost);
        }
        return carClub.isEmpty() ? new ResponseEntity<>(HttpStatus.CONFLICT)
                : new ResponseEntity<>(newClubPost, HttpStatus.CREATED);

    }

}