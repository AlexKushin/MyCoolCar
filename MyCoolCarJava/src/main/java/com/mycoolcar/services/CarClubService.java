package com.mycoolcar.services;

import com.mycoolcar.dtos.CarClubCreationDto;
import com.mycoolcar.dtos.CarClubDto;
import com.mycoolcar.entities.CarClub;
import com.mycoolcar.entities.User;
import com.mycoolcar.enums.CarClubAccessType;
import com.mycoolcar.exceptions.IncorrectCarClubAccessTypeException;
import com.mycoolcar.exceptions.ResourceNotFoundException;
import com.mycoolcar.mapper.CarClubDtoMapper;
import com.mycoolcar.repositories.CarClubPostRepository;
import com.mycoolcar.repositories.CarClubRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final FileService fileService;


    @Autowired
    public CarClubService(CarClubRepository carClubRepository,
                          CarClubPostRepository carClubPostRepository,
                          UserService userService,
                          CarClubDtoMapper carClubDtoMapper,
                          FileService fileService) {
        this.carClubRepository = carClubRepository;
        this.carClubPostRepository = carClubPostRepository;
        this.userService = userService;
        this.carClubDtoMapper = carClubDtoMapper;
        this.fileService = fileService;
    }

    public CarClubDto saveNewCarClub(CarClubCreationDto carClubCreationDto,
                                     MultipartFile mainImage, String email) throws IOException {
        User user = userService.getUserByEmail(email);
        CarClub carClub = new CarClub(carClubCreationDto.name(), carClubCreationDto.description(),
                carClubCreationDto.location(), user);
        if (carClubCreationDto.accessType().equalsIgnoreCase("private")) {
            carClub.setAccessType(CarClubAccessType.PRIVATE);
        }
        if (!mainImage.isEmpty()) {
            log.info("Uploading main image for the new car club");
            String mainImageUrl = fileService.uploadFile(mainImage);
            carClub.setMainImageUrl(mainImageUrl);
        }
        return carClubDtoMapper.apply(carClubRepository.save(carClub));
    }

    public List<CarClubDto> getCarClubs() {
        return carClubRepository.findAll().stream().map(carClubDtoMapper).collect(Collectors.toList());
    }

    public CarClub getCarClubById(Long carClubId) {
        log.info("Getting CarClub by id: {}", carClubId);
        Optional<CarClub> carClub = carClubRepository.findById(carClubId);
        return carClub.orElseThrow(() -> new ResourceNotFoundException("Car Club with email " + carClub + " not found"));

    }

    public CarClubDto addMemberToPublicCarClub(String email, Long carClubId) {
        CarClub carClub = getCarClubById(carClubId);
        if (carClub.getAccessType() == CarClubAccessType.PRIVATE) {
            throw new IncorrectCarClubAccessTypeException("Car club is private");
        }
        User user = userService.getUserByEmail(email);
        carClub.addCarClubMember(user);
        return carClubDtoMapper.apply(carClubRepository.save(carClub));
    }

    public CarClubDto addUserToPrivateCarClubWaitList(String email, Long carClubId) {
        CarClub carClub = getCarClubById(carClubId);
        if (carClub.getAccessType() == CarClubAccessType.PUBLIC) {
            //todo add to exception handler
            throw new IncorrectCarClubAccessTypeException("Car club is public");
        }
        User user = userService.getUserByEmail(email);
        System.out.println(user.getId());
        carClub.addToWaitlist(user);
        return carClubDtoMapper.apply(carClubRepository.save(carClub));
    }

    public CarClubDto confirmUserMembership(Long carClubId, String email, Long userIdToConfirm) {
        CarClub carClub = getCarClubById(carClubId);
        User clubOwner = userService.getUserByEmail(email);
        if (!clubOwner.equals(carClub.getClubOwner())) {
            //todo: handle error if current user is not car club owner
            throw new RuntimeException();
        }
        User userToConfirm = userService.getUserById(userIdToConfirm);
        carClub.removeFromWaitlist(userToConfirm);
        carClub.addCarClubMember(userToConfirm);
        return carClubDtoMapper.apply(carClubRepository.save(carClub));
    }

    public CarClubDto refuseUserMembership(Long carClubId, String email, Long userIdToRefuse) {
        CarClub carClub = getCarClubById(carClubId);
        User clubOwner = userService.getUserByEmail(email);
        if (!clubOwner.equals(carClub.getClubOwner())) {
            //todo: handle error if current user is not car club owner
            throw new RuntimeException();
        }
        User userToConfirm = userService.getUserById(userIdToRefuse);
        carClub.removeFromWaitlist(userToConfirm);
        return carClubDtoMapper.apply(carClubRepository.save(carClub));
    }

    public CarClubDto removeCarClubMember(String email, Long carClubId) {
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
