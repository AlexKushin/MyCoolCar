package com.mycoolcar.services;

import com.mycoolcar.dtos.CarClubCreationDto;
import com.mycoolcar.dtos.CarClubDto;
import com.mycoolcar.entities.CarClub;
import com.mycoolcar.entities.User;
import com.mycoolcar.enums.CarClubAccessType;
import com.mycoolcar.exceptions.ResourceNotFoundException;
import com.mycoolcar.mapper.CarClubDtoMapper;
import com.mycoolcar.repositories.CarClubPostRepository;
import com.mycoolcar.repositories.CarClubRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CarClubService {
    private final CarClubRepository carClubRepository;
    private final CarClubPostRepository carClubPostRepository;
    private final UserService userService;
    private final CarClubDtoMapper carClubDtoMapper;


    @Autowired
    public CarClubService(CarClubRepository carClubRepository,
                         CarClubPostRepository carClubPostRepository,
                          UserService userService,
                          CarClubDtoMapper carClubDtoMapper) {
        this.carClubRepository = carClubRepository;
        this.carClubPostRepository = carClubPostRepository;
        this.userService = userService;
        this.carClubDtoMapper = carClubDtoMapper;
    }

    public CarClub saveNewCarClub(CarClubCreationDto carClubCreationDto, String email) {
        User user = userService.getUserByEmail(email);

        CarClub carClub = new CarClub();
        carClub.setName(carClubCreationDto.name());
        carClub.setDescription(carClubCreationDto.description());
        if (carClubCreationDto.accessType().equalsIgnoreCase("private")) {
            carClub.setAccessType(CarClubAccessType.PRIVATE);
        } else {
            carClub.setAccessType(CarClubAccessType.PUBLIC);
        }
        carClub.setCreatedTime(LocalDateTime.now());
        carClub.setLocation(carClubCreationDto.location());
        carClub.setClubOwner(user);
        carClub.addCarClubMember(user);
        return carClubRepository.save(carClub);
    }

    public List<CarClubDto> getCarClubs() {
        return carClubRepository.findAll().stream().map(carClubDtoMapper).collect(Collectors.toList());
    }



    public CarClub getCarClubById(Long carClubId) {
        log.info("Getting CarClub by id: {}", carClubId);
        Optional<CarClub> carClub = carClubRepository.findById(carClubId);
        if (carClub.isEmpty()) {
            throw new ResourceNotFoundException("User with email " + carClub + " not found");
        }
        return carClub.get();
    }

    public CarClubDto addCarClubMember(String email, Long carClubId){
        CarClub carClub = getCarClubById(carClubId);
        User user = userService.getUserByEmail(email);
        carClub.addCarClubMember(user);
        return carClubDtoMapper.apply(carClubRepository.save(carClub));
    }

    public CarClubDto removeCarClubMember(String email, Long carClubId){
        CarClub carClub = getCarClubById(carClubId);
        User user = userService.getUserByEmail(email);
        carClub.removeCarClubMember(user);
        return carClubDtoMapper.apply(carClubRepository.save(carClub));
    }

    public Set<CarClubDto> getUserCarClubs(String email) {
        User user = userService.getUserByEmail(email);
        return user.getUserClubs().stream().map(carClubDtoMapper).collect(Collectors.toSet());
    }
}
