package com.mycoolcar.controllers;

import com.mycoolcar.dtos.CarClubCreationDto;
import com.mycoolcar.entities.CarClub;
import com.mycoolcar.entities.User;
import com.mycoolcar.services.CarClubService;
import com.mycoolcar.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/")
public class CarClubController {

    private final CarClubService carClubService;
    private final UserService userService;

    public CarClubController(CarClubService carClubService, UserService userService) {
        this.carClubService = carClubService;
        this.userService = userService;
    }

    @PostMapping("car_clubs/new")
    public ResponseEntity<CarClub> postCar(Principal principal, @RequestBody CarClubCreationDto carClubCreationDto) throws IOException {

        User user = userService.getUserByEmail(principal.getName());

        CarClub carClub = carClubService.saveNewCarClub(carClubCreationDto, user);
        return new ResponseEntity<>(carClub, HttpStatus.CREATED);
    }

    @GetMapping("car_clubs")
    public ResponseEntity<List<CarClub>> getAllCarClubs() {
        return new ResponseEntity<>(carClubService.getCarClubs(), HttpStatus.OK);
    }

    @GetMapping("car_clubs/my")
    public ResponseEntity<List<CarClub>> getUserCarClubs(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        List<CarClub> userCarClubs = user.getUserClubs();
        return new ResponseEntity<>(userCarClubs, HttpStatus.OK);
    }

}
