package com.mycoolcar.controllers;

import com.mycoolcar.dtos.UserCreationDto;
import com.mycoolcar.entities.Post;
import com.mycoolcar.entities.User;
import com.mycoolcar.services.PostService;
import com.mycoolcar.services.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;
    private final PostService postService;


    @Autowired
    public UserController(UserService userService,
                          PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @PostConstruct
    public void initRolesAndUsers() {
        userService.initRolesAndUsers();
    }

    @PostMapping("/user/registration")
    public ResponseEntity<User> registerNewUser(@Valid @RequestBody  UserCreationDto userCreationDto) {
        Optional<User> result = userService.registerNewUser(userCreationDto);
        return result.isEmpty() ? new ResponseEntity<>(HttpStatus.CONFLICT)
                : new ResponseEntity<>(result.get(), HttpStatus.CREATED);
    }


    @GetMapping("/persons")
    public ResponseEntity<UserCreationDto> getPerson() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/persons")
    public ResponseEntity<UserCreationDto> putPerson() {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping({"/admin"})
    public String forAdmin() {
        return "this URL is only accessible for Admin";
    }


    @GetMapping({"/user"})
    public User getUser() {
        Optional<User> user = userService.getByEmail("user@gmail.com");
        return user.get();
    }

    @GetMapping({"/me"})
    public ResponseEntity<User> getMe(Principal principal) {
        Optional<User> user = userService.getByUsername(principal.getName());
        return user.isEmpty() ? new ResponseEntity<>(HttpStatus.CONFLICT)
                : new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @GetMapping("user/news")
    public ResponseEntity<List<Post>> getNewPosts(Principal principal) {
        Optional<User> user = userService.getByUsername(principal.getName());
        return user.isEmpty() ? new ResponseEntity<>(HttpStatus.CONFLICT)
                : new ResponseEntity<>(postService.getNewPosts(user.get()), HttpStatus.OK);
    }
}
