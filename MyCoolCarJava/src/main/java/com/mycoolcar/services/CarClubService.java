package com.mycoolcar.services;

import com.mycoolcar.dtos.CarClubCreationDto;
import com.mycoolcar.dtos.CarDto;
import com.mycoolcar.entities.CarClub;
import com.mycoolcar.entities.User;
import com.mycoolcar.enums.CarClubAccessType;
import com.mycoolcar.repositories.CarClubPostRepository;
import com.mycoolcar.repositories.CarClubRepository;
import com.mycoolcar.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarClubService {
    private final CarClubRepository carClubRepository;
    private final UserRepository userRepository;
    private final CarClubPostRepository carClubPostRepository;


    @Autowired
    public CarClubService(CarClubRepository carClubRepository,
                          UserRepository userRepository, CarClubPostRepository carClubPostRepository) {
        this.carClubRepository = carClubRepository;
        this.userRepository = userRepository;
        this.carClubPostRepository = carClubPostRepository;
    }

    public CarClub saveNewCarClub(CarClubCreationDto carClubCreationDto, User user) {
        CarClub carClub = new CarClub();
        carClub.setName(carClubCreationDto.name());
        carClub.setDescription(carClubCreationDto.description());
        if (carClubCreationDto.accessType().equalsIgnoreCase("private")) {
            carClub.setAccessType(CarClubAccessType.PRIVATE);
        } else {
            carClub.setAccessType(CarClubAccessType.PUBLIC);
        }
        carClub.setClubOwner(user);
        CarClub savedCarClub = carClubRepository.save(carClub);
        user.getUserClubs().add(savedCarClub);
        userRepository.save(user);
        return savedCarClub;
    }

    public List<CarClub> getCarClubs() {
        return carClubRepository.findAll();
    }

}
