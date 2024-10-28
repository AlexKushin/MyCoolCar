package com.mycoolcar.controllers;

import com.mycoolcar.dtos.CarClubCreationDto;
import com.mycoolcar.dtos.CarClubDto;
import com.mycoolcar.services.CarClubService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("api/")
public class CarClubController {

    private final CarClubService carClubService;

    public CarClubController(CarClubService carClubService) {
        this.carClubService = carClubService;
    }

    @PostMapping(path = "car_clubs/new")
    public ResponseEntity<CarClubDto> postCarClub(Principal principal,
                                                  @RequestPart("mainImage") MultipartFile mainImage,
                                                  @ModelAttribute CarClubCreationDto carClubCreationDto) throws IOException {
        CarClubDto carClub = carClubService.saveNewCarClub(carClubCreationDto, mainImage, principal.getName());
        return new ResponseEntity<>(carClub, HttpStatus.CREATED);
    }

    @GetMapping("car_clubs")
    public ResponseEntity<List<CarClubDto>> getAllCarClubs() {
        return new ResponseEntity<>(carClubService.getCarClubs(), HttpStatus.OK);
    }

    @GetMapping("car_clubs/my")
    public ResponseEntity<Set<CarClubDto>> getUserCarClubs(Principal principal) {
        Set<CarClubDto> userCarClubs = carClubService.getUserCarClubs(principal.getName());
        return new ResponseEntity<>(userCarClubs, HttpStatus.OK);
    }

    @PostMapping("car_clubs/{id}/join")
    public ResponseEntity<CarClubDto> joinCarClub(@PathVariable long id, Principal principal) {
        CarClubDto updatedCarClub = carClubService.addMemberToPublicCarClub(principal.getName(), id);
        return new ResponseEntity<>(updatedCarClub, HttpStatus.OK);
    }

    @PostMapping("car_clubs/{id}/leave")
    public ResponseEntity<CarClubDto> leaveCarClub(@PathVariable long id, Principal principal) {
        CarClubDto updatedCarClub = carClubService.removeCarClubMember(principal.getName(), id);
        return new ResponseEntity<>(updatedCarClub, HttpStatus.OK);
    }

    @PostMapping("car_clubs/{id}/join/private")
    public ResponseEntity<CarClubDto> joinToPrivateCarClub(@PathVariable long id, Principal principal) {
        CarClubDto updatedCarClub = carClubService.addUserToPrivateCarClubWaitList(principal.getName(), id);
        return new ResponseEntity<>(updatedCarClub, HttpStatus.OK);
    }

    @PostMapping("car_clubs/{carClubId}/members/confirm")
    public ResponseEntity<CarClubDto> confirmCarClubMember(@PathVariable long carClubId, Principal principal,
                                                           @RequestParam long userId) {
        CarClubDto updatedCarClub = carClubService.confirmUserMembership(carClubId, principal.getName(), userId);
        return new ResponseEntity<>(updatedCarClub, HttpStatus.OK);
    }

    @PostMapping("car_clubs/{carClubId}/members/refuse")
    public ResponseEntity<CarClubDto> refuseCarClubMember(@PathVariable long carClubId, Principal principal,
                                                          @RequestParam long userId) {
        CarClubDto updatedCarClub = carClubService.refuseUserMembership(carClubId, principal.getName(), userId);
        return new ResponseEntity<>(updatedCarClub, HttpStatus.OK);
    }
}
