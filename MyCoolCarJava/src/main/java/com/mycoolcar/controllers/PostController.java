package com.mycoolcar.controllers;

import com.mycoolcar.dtos.CarLogPostDto;
import com.mycoolcar.dtos.ClubPostDto;
import com.mycoolcar.entities.*;
import com.mycoolcar.exceptions.ApiResponse;
import com.mycoolcar.repositories.CarClubRepository;
import com.mycoolcar.repositories.CarLogbookRepository;
import com.mycoolcar.services.PostService;
import com.mycoolcar.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/")
@CrossOrigin(origins = "http://localhost:4200")
public class PostController {

    private final PostService postService;
    private final CarLogbookRepository carLogbookRepository;
    private final CarClubRepository carClubRepository;

    private final UserService userService;

    private final MessageSource messageSource;

    @Autowired
    public PostController(PostService postService, CarLogbookRepository carLogbookRepository,
                          CarClubRepository carClubRepository, UserService userService, MessageSource messageSource) {
        this.postService = postService;
        this.carLogbookRepository = carLogbookRepository;
        this.carClubRepository = carClubRepository;
        this.userService = userService;
        this.messageSource = messageSource;
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

    @GetMapping("user/news")
    public ResponseEntity<List<Post>> getNewPosts(Principal principal) {
        Optional<User> user = userService.getByUsername(principal.getName());
        return user.map(value -> new ResponseEntity<>(postService.getNewPosts(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.CONFLICT));
    }

    @DeleteMapping({"/car-club-posts/{id}"})
    public ResponseEntity<ApiResponse> deleteCarClubPost(@PathVariable long id, final Locale locale) {
        postService.deleteCarClubPost(id);
        return new ResponseEntity<>(new ApiResponse(
                messageSource.getMessage("message.deletePost", null, locale)), HttpStatus.OK);
    }
    @DeleteMapping({"/car-logbook-posts/{id}"})
    public ResponseEntity<ApiResponse> deleteCarLogBookPost(@PathVariable long id, final Locale locale) {
        postService.deleteCarLogPost(id);
        return new ResponseEntity<>(new ApiResponse(
                messageSource.getMessage("message.deletePost", null, locale)), HttpStatus.OK);
    }

}
