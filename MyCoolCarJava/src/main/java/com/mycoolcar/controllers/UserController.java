package com.mycoolcar.controllers;

import com.mycoolcar.dtos.UserCreationDto;
import com.mycoolcar.entities.User;
import com.mycoolcar.services.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void initRolesAndUsers() {
        userService.initRolesAndUsers();
    }

    @PostMapping("/persons")
    public ResponseEntity<User> registerNewPerson(@RequestBody UserCreationDto userCreationDto) {
        Optional<User> result = userService.registerNewUser(userCreationDto);
        return result.isEmpty() ? new ResponseEntity<>(HttpStatus.CONFLICT)
                : new ResponseEntity<>(result.get(), HttpStatus.CREATED);
    }

    /*@GetMapping("/user/registration")
    public String showRegistrationForm(WebRequest request, Model model) {
        PersonDto personDto = new PersonDto();
        model.addAttribute("person", personDto);
        return "registration";
    }
*/
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
    public String forUser() {
        return "this URL is only accessible for User";
    }
}
