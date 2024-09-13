package com.mycoolcar.controllers;

import com.mycoolcar.dtos.NewPasswordDto;
import com.mycoolcar.dtos.UserCreationDto;
import com.mycoolcar.dtos.UserDto;
import com.mycoolcar.entities.User;
import com.mycoolcar.entities.VerificationToken;
import com.mycoolcar.util.ApiResponse;
import com.mycoolcar.exceptions.UserNotFoundException;
import com.mycoolcar.registration.OnRegistrationCompleteEvent;
import com.mycoolcar.registration.OnResetPasswordEvent;
import com.mycoolcar.services.UserService;
import com.mycoolcar.util.MessageSourceHandler;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.security.Principal;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")

public class UserController {
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;
    private final MessageSourceHandler messageSourceHandler;

    @Autowired
    public UserController(UserService userService,
                          ApplicationEventPublisher eventPublisher,
                          MessageSourceHandler messageSourceHandler) {
        this.userService = userService;
        this.eventPublisher = eventPublisher;
        this.messageSourceHandler = messageSourceHandler;
    }

    /*@PostConstruct
    public void initRolesAndUsers() {
        userService.initRolesAndUsers();
    }*/

    @PostMapping("/user/registration")
    public ResponseEntity<User> registerNewUser(@Valid @RequestBody UserCreationDto userCreationDto,
                                                WebRequest request) {

        log.debug("Registering user account with information: {}", userCreationDto);
        Optional<User> registered = userService.registerNewUserAccount(userCreationDto);
        if (registered.isPresent()) {
            String appUrl = getAppUrl(request);
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered.get(), request, appUrl));
        }
        return registered.isEmpty() ? new ResponseEntity<>(HttpStatus.CONFLICT)
                : new ResponseEntity<>(registered.get(), HttpStatus.CREATED);
    }

    @GetMapping("/registration/confirm")
    public ResponseEntity<ApiResponse> confirmRegistration
            (WebRequest request, @RequestParam("token") String token) {
        String result = userService.validatePasswordResetToken(token);
        if (result != null) {
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.BAD_REQUEST, messageSourceHandler
                            .getLocalMessage("auth.message." + result, request, "Invalid token")),
                    HttpStatus.BAD_REQUEST);
        }
        VerificationToken verificationToken = userService.getVerificationToken(token);
        User user = verificationToken.getUser();
        user.setEnabled(true);
        userService.saveRegisteredUser(user);
        userService.deleteVerificationToken(token);
        String message = messageSourceHandler
                .getLocalMessage("auth.message.confirm", request, "registration confirmed");
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, message), HttpStatus.OK);
    }

    @PostMapping("/user/resetPassword")
    public ResponseEntity<ApiResponse> resetPassword(WebRequest request,
                                                     @RequestParam("email") String userEmail) {
        Optional<User> user = userService.getUserByEmail(userEmail);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with email " + userEmail + " not found");
        }
        String appUrl = getAppUrl(request);
        eventPublisher.publishEvent(new OnResetPasswordEvent(user.get(),
                request, appUrl));
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,
                messageSourceHandler
                        .getLocalMessage("message.resetPasswordEmail", request,
                                "You should receive and Password Reset Email shortly")), HttpStatus.OK);
    }

    @PostMapping("/user/savePassword")
    public ResponseEntity<ApiResponse> savePassword(WebRequest request,
                                                    @Valid @RequestBody NewPasswordDto passwordDto) {
        String result = userService.validatePasswordResetToken(passwordDto.token());

        if (result != null) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, messageSourceHandler.getLocalMessage(
                    "auth.message." + result, request, "Invalid token")), HttpStatus.BAD_REQUEST);
        }

        Optional<User> user = userService.getUserByVerificationToken(passwordDto.token());
        if (user.isPresent()) {
            userService.changeUserPassword(user.get(), passwordDto.password());
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, messageSourceHandler.getLocalMessage(
                    "message.resetPasswordSuc", request, "Password reset successfully")),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.BAD_REQUEST, messageSourceHandler.getLocalMessage(
                            "auth.message.invalid", request,
                            "This username is invalid, or does not exist")), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping({"/admin"})
    public String forAdmin() {
        return "this URL is only accessible for Admin";
    }

    @PutMapping({"/admin/users/{id}"})
    public ResponseEntity<User> banUser(@PathVariable long id) {
        Optional<User> bannedUser = userService.banUser(id);
        return bannedUser.isEmpty() ? new ResponseEntity<>(HttpStatus.CONFLICT)
                : new ResponseEntity<>(bannedUser.get(), HttpStatus.OK);
    }

    @DeleteMapping({"/admin/users/{id}"})
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable long id, WebRequest request) {
        userService.deleteUser(id);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,
                messageSourceHandler
                        .getLocalMessage("message.deleteUser", request,
                                "User has been deleted successfully")), HttpStatus.OK);
    }


    @GetMapping({"/me"})
    public ResponseEntity<UserDto> getMe(Principal principal) {
        UserDto user = userService.getUserDtoByEmail(principal.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);

    }

    //non Api
    private String getAppUrl(WebRequest request) {
        return request.getHeader("Origin") + request.getContextPath();
    }

}
