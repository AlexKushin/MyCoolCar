package com.mycoolcar.controllers;

import com.mycoolcar.dtos.NewPasswordDto;
import com.mycoolcar.dtos.UserCreationDto;
import com.mycoolcar.dtos.UserDto;
import com.mycoolcar.util.ApiResponse;
import com.mycoolcar.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.security.Principal;


@Slf4j
@RestController
@RequestMapping("/api")

public class UserController {
    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*@PostConstruct
    public void initRolesAndUsers() {
        userService.initRolesAndUsers();
    }*/

    @PostMapping("/user/registration")
    public ResponseEntity<UserDto> registerNewUser(@Valid @RequestBody UserCreationDto userCreationDto,
                                                WebRequest request) {
        log.debug("Registering user account with information: {}", userCreationDto);
        UserDto registeredUser = userService.registerNewUserAccount(userCreationDto, request);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @GetMapping("/registration/confirm")
    public ResponseEntity<ApiResponse> confirmRegistration
            (WebRequest request, @RequestParam("token") String token) {
        ApiResponse response = userService.confirmRegistration(request, token);
        return new ResponseEntity<>(response, response.statusCode());
    }

    @PostMapping("/user/resetPassword")
    public ResponseEntity<ApiResponse> resetPassword(WebRequest request, @RequestParam("email") String userEmail) {
        ApiResponse response =  userService.resetPassword(request, userEmail);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/user/savePassword")
    public ResponseEntity<ApiResponse> savePassword(WebRequest request, @Valid @RequestBody NewPasswordDto passwordDto) {
        ApiResponse response = userService.savePassword(request, passwordDto);
        return new ResponseEntity<>(response, response.statusCode());
    }


    @GetMapping({"/admin"})
    public String forAdmin() {
        return "this URL is only accessible for Admin";
    }

    @PutMapping({"/admin/users/{id}"})
    public ResponseEntity<UserDto> banUser(@PathVariable long id) {
        UserDto bannedUser = userService.banUser(id);
        return  new ResponseEntity<>(bannedUser, HttpStatus.OK);
    }

    @DeleteMapping({"/admin/users/{id}"})
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable long id, WebRequest request) {
        return new ResponseEntity<>(userService.deleteUser(id, request), HttpStatus.OK);
    }


    @GetMapping({"/me"})
    public ResponseEntity<UserDto> getMe(Principal principal) {
        UserDto user = userService.getUserDtoByEmail(principal.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);

    }
}
