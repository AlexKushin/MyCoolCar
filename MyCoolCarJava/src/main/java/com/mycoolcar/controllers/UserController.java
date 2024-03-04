package com.mycoolcar.controllers;

import com.mycoolcar.dtos.UserCreationDto;
import com.mycoolcar.entities.Post;
import com.mycoolcar.entities.User;
import com.mycoolcar.entities.VerificationToken;
import com.mycoolcar.exceptions.ApiResponse;
import com.mycoolcar.registration.OnRegistrationCompleteEvent;
import com.mycoolcar.services.PostService;
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
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;
    private final PostService postService;

    private final ApplicationEventPublisher eventPublisher;
    private final MessageSource messageSource;


    @Autowired
    public UserController(UserService userService,
                          PostService postService,
                          ApplicationEventPublisher eventPublisher, MessageSource messageSource) {
        this.userService = userService;
        this.postService = postService;
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
        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            String message = messageSource.getMessage("auth.message.invalidToken", null, locale);
            return new ResponseEntity<>(
                    new ApiResponse(message, "Verification token == null"), HttpStatus.FORBIDDEN);
        }
        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
               String message = messageSource.getMessage("auth.message.expired", null, locale);
            return new ResponseEntity<>(
                    new ApiResponse(message, "Invalid verification token"), HttpStatus.FORBIDDEN);
        }
        user.setEnabled(true);
        userService.saveRegisteredUser(user);
        String message = messageSource.getMessage("auth.message.confirm", null, locale);
        return new ResponseEntity<>(new ApiResponse(message), HttpStatus.OK);
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


    private String getAppUrl(HttpServletRequest request) {
        return request.getHeader("Origin") + request.getContextPath();
    }


}
