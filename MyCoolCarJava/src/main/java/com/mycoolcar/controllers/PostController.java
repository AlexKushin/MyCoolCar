package com.mycoolcar.controllers;

import com.mycoolcar.dtos.PostCreationDto;
import com.mycoolcar.dtos.PostDto;
import com.mycoolcar.entities.*;
import com.mycoolcar.util.ApiResponse;
import com.mycoolcar.services.PostService;
import com.mycoolcar.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/")
public class PostController {

    private final PostService postService;

    private final UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping("car-logbook/{carLogbookId}/car-log-posts/new")
    public ResponseEntity<PostDto> postCarLog(@PathVariable Long carLogbookId,
                                              @RequestBody PostCreationDto postCreationDto) {
        PostDto newCarLogPost = postService.postCarLogbookPost(carLogbookId, postCreationDto);
        return new ResponseEntity<>(newCarLogPost, HttpStatus.CREATED);

    }

    @PostMapping("car-club/{carClubId}/club-posts/new")
    public ResponseEntity<PostDto> postClubPost(@PathVariable Long carClubId,
                                                @RequestBody PostCreationDto postCreationDto) {
        PostDto newClubPost = postService.postCarClubPost(carClubId, postCreationDto);
        return new ResponseEntity<>(newClubPost, HttpStatus.CREATED);
    }

    @GetMapping("user/news")
    public ResponseEntity<List<Post>> getNewPosts(Principal principal) {
        Optional<User> user = userService.getByUsername(principal.getName());
        return user.map(value -> new ResponseEntity<>(postService.getNewPosts(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.CONFLICT));
    }

    @DeleteMapping({"/car-club-posts/{id}"})
    public ResponseEntity<ApiResponse> deleteCarClubPost(@PathVariable long id, WebRequest request) {
        ApiResponse response = postService.deleteCarClubPost(id, request);
        return new ResponseEntity<>(response, response.statusCode());
    }

    @DeleteMapping({"/car-logbook-posts/{id}"})
    public ResponseEntity<ApiResponse> deleteCarLogBookPost(@PathVariable long id, WebRequest request) {
        ApiResponse response = postService.deleteCarLogPost(id, request);
        return new ResponseEntity<>(response, response.statusCode());
    }

    @PutMapping({"/car-logbook-posts/{id}"})
    public ResponseEntity<PostDto> editCarLogbookPost(@PathVariable long id,
                                                      @RequestBody PostCreationDto postCreationDto) {
        PostDto editedCarLogbookPost = postService.editCarLogbookPost(id, postCreationDto);
        return new ResponseEntity<>(editedCarLogbookPost, HttpStatus.OK);
    }

    @GetMapping({"/cars/{carId}/logbook"})
    public ResponseEntity<CarLogbook> getCarLogbookByCarId(@PathVariable Long carId) {
        CarLogbook carLogbook = postService.getCarLogbookByCarId(carId);
        return new ResponseEntity<>(carLogbook, HttpStatus.OK);
    }
}
