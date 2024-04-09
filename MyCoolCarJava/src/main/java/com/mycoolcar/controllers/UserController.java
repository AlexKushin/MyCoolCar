package com.mycoolcar.controllers;

import com.mycoolcar.dtos.NewPasswordDto;
import com.mycoolcar.dtos.UserCreationDto;
import com.mycoolcar.entities.User;
import com.mycoolcar.entities.VerificationToken;
import com.mycoolcar.exceptions.ApiResponse;
import com.mycoolcar.exceptions.UserNotFoundException;
import com.mycoolcar.registration.OnRegistrationCompleteEvent;
import com.mycoolcar.registration.OnResetPasswordEvent;
import com.mycoolcar.services.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
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
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;
    private final MessageSource messageSource;

    @Autowired
    public UserController(UserService userService,
                          ApplicationEventPublisher eventPublisher,
                          MessageSource messageSource) {
        this.userService = userService;
        this.eventPublisher = eventPublisher;
        this.messageSource = messageSource;
    }

    @PostConstruct
    public void initRolesAndUsers() {
        userService.initRolesAndUsers();
    }

    @PostMapping("/user/registration")
    public ResponseEntity<User> registerNewUser(@Valid @RequestBody UserCreationDto userCreationDto,
                                                HttpServletRequest request) {
        log.debug("Registering user account with information: {}", userCreationDto);
        Optional<User> registered = userService.registerNewUserAccount(userCreationDto);
        if (registered.isPresent()) {
            String appUrl = getAppUrl(request);
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered.get(),
                    request.getLocale(), appUrl));
        }
        return registered.isEmpty() ? new ResponseEntity<>(HttpStatus.CONFLICT)
                : new ResponseEntity<>(registered.get(), HttpStatus.CREATED);
    }

    @GetMapping("/registration/confirm")
    public ResponseEntity<ApiResponse> confirmRegistration
            (WebRequest request, @RequestParam("token") String token) {
        final Locale locale = request.getLocale();
        String result = userService.validatePasswordResetToken(token);
        if(result != null) {
            return new ResponseEntity<>(new ApiResponse(messageSource.getMessage(
                    "auth.message." + result, null, locale)),HttpStatus.BAD_REQUEST);
        }
        VerificationToken verificationToken = userService.getVerificationToken(token);
        User user = verificationToken.getUser();
        user.setEnabled(true);
        userService.saveRegisteredUser(user);
        userService.deleteVerificationToken(token);
        String message = messageSource.getMessage("auth.message.confirm", null, locale);
        return new ResponseEntity<>(new ApiResponse(message), HttpStatus.OK);
    }

    @PostMapping("/user/resetPassword")
    public ResponseEntity<ApiResponse> resetPassword(HttpServletRequest request,
                                     @RequestParam("email") String userEmail) {
        Optional <User> user = userService.getUserByEmail(userEmail);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with email "+ userEmail + " not found");
        }
        String appUrl = getAppUrl(request);
        eventPublisher.publishEvent(new OnResetPasswordEvent(user.get(),
                request.getLocale(), appUrl));
        return new ResponseEntity<>(new ApiResponse(
                messageSource.getMessage("message.resetPasswordEmail", null,
                        request.getLocale())), HttpStatus.OK);
    }

    @PostMapping("/user/savePassword")
    public ResponseEntity<ApiResponse> savePassword(final Locale locale,
                                                    @Valid @RequestBody NewPasswordDto passwordDto) {
        String result = userService.validatePasswordResetToken(passwordDto.token());

        if(result != null) {
            return new ResponseEntity<>(new ApiResponse(messageSource.getMessage(
                    "auth.message." + result, null, locale)),HttpStatus.BAD_REQUEST);
        }

        Optional<User> user = userService.getUserByVerificationToken(passwordDto.token());
        if(user.isPresent()) {
            userService.changeUserPassword(user.get(), passwordDto.password());
            return new ResponseEntity<>(new ApiResponse(messageSource.getMessage(
                    "message.resetPasswordSuc", null, locale)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(messageSource.getMessage(
                    "auth.message.invalid", null, locale)), HttpStatus.BAD_REQUEST);
        }
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

    @PutMapping({"/admin/users/{id}"})
    public ResponseEntity<User> banUser(@PathVariable long id) {
        Optional<User> bannedUser = userService.banUser(id);
        return bannedUser.isEmpty() ? new ResponseEntity<>(HttpStatus.CONFLICT)
                : new ResponseEntity<>(bannedUser.get(), HttpStatus.OK);
    }

    @DeleteMapping({"/admin/users/{id}"})
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable long id, final Locale locale) {
        userService.deleteUser(id);
        return new ResponseEntity<>(new ApiResponse(
                messageSource.getMessage("message.deleteUser", null, locale)), HttpStatus.OK);
    }


    @GetMapping({"/me"})
    public ResponseEntity<User> getMe(Principal principal) {
        Optional<User> user = userService.getUserByEmail(principal.getName());
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.CONFLICT));
    }

    //non Api
    private String getAppUrl(HttpServletRequest request) {
        return request.getHeader("Origin") + request.getContextPath();
    }

}
