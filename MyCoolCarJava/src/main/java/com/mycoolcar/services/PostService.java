package com.mycoolcar.services;

import com.mycoolcar.dtos.PostCreationDto;
import com.mycoolcar.dtos.PostDto;
import com.mycoolcar.entities.*;
import com.mycoolcar.exceptions.ResourceNotFoundException;
import com.mycoolcar.repositories.CarClubPostRepository;
import com.mycoolcar.repositories.CarClubRepository;
import com.mycoolcar.repositories.CarLogBookPostRepository;
import com.mycoolcar.repositories.CarLogbookRepository;
import com.mycoolcar.util.ApiResponse;
import com.mycoolcar.util.MessageSourceHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Slf4j
public class PostService {

    private final CarLogBookPostRepository carLogBookPostRepository;
    private final CarClubPostRepository carClubPostRepository;
    private final CarLogbookRepository carLogbookRepository;
    private final CarClubRepository carClubRepository;
    private final MessageSourceHandler messageSourceHandler;

    @Autowired
    public PostService(CarLogBookPostRepository carLogBookPostRepository,
                       CarClubPostRepository carClubPostRepository,
                       CarLogbookRepository carLogbookRepository,
                       CarClubRepository carClubRepository,
                       MessageSourceHandler messageSourceHandler) {
        this.carLogBookPostRepository = carLogBookPostRepository;
        this.carClubPostRepository = carClubPostRepository;
        this.carLogbookRepository = carLogbookRepository;
        this.carClubRepository = carClubRepository;
        this.messageSourceHandler = messageSourceHandler;
    }

    public PostDto postCarLogbookPost(Long carLogbookId, PostCreationDto postCreationDto) {
        CarLogbook carLogbook = getCarLogbookById(carLogbookId);
        CarLogPost newCarLogPost = new CarLogPost();
        newCarLogPost.setTopic(postCreationDto.topic());
        newCarLogPost.setDescription(postCreationDto.description());
        newCarLogPost.setCarLogbook(carLogbook);
        newCarLogPost.setCreatedTime(LocalDateTime.now());
        CarLogPost savedPost = carLogBookPostRepository.save(newCarLogPost);
        log.info("CarLogPost saved with ID: {}", savedPost.getId());
        return mapPostToDto(savedPost);
    }

    public PostDto postCarClubPost(Long carClubId, PostCreationDto postCreationDto) {
        CarClub carClub = getCarClubById(carClubId);
        ClubPost newCarClubPost = new ClubPost();
        newCarClubPost.setCarClub(carClub);
        newCarClubPost.setTopic(postCreationDto.topic());
        newCarClubPost.setDescription(postCreationDto.description());
        newCarClubPost.setCreatedTime(LocalDateTime.now());
        ClubPost savedPost = carClubPostRepository.save(newCarClubPost);
        log.info("CarClubPost saved with ID: {}", savedPost.getId());
        return mapPostToDto(savedPost);
    }



    public List<Post> getNewPosts(User user) {
        log.info("Fetching new posts for user: {}", user.getId());
        List<CarLogbook> carLogbooks = carLogbookRepository.findAllByCarIn(user.getSubscribedCars());
        List<Post> newPosts = Stream.concat(
                        carLogBookPostRepository.findAllByCarLogbookInOrderByCreatedTime(carLogbooks).stream(),
                        carClubPostRepository.findAllByCarClubInOrderByCreatedTime(user.getUserClubs()).stream())
                .toList();
        log.info("Fetched {} new posts for user: {}", newPosts.size(), user.getId());
        return newPosts;
    }

    public ApiResponse deleteCarLogPost(long id, WebRequest request) {
        log.info("Deleting CarLogPost with ID: {}", id);
        CarLogPost carLogPostToDelete = getCarLogPostById(id);
        carLogBookPostRepository.delete(carLogPostToDelete);
        log.info("Deleted CarLogPost with ID: {}", id);
        return new ApiResponse(HttpStatus.OK,
                messageSourceHandler.getLocalMessage("message.deletePost", request,
                                "Post has been deleted"));
    }

    public ApiResponse deleteCarClubPost(long id, WebRequest request) {
        log.info("Deleting CarClubPost with ID: {}", id);
        ClubPost clubPost = getClubPostById(id);
        carClubPostRepository.delete(clubPost);
        log.info("Deleted CarClubPost with ID: {}", id);
        return new ApiResponse(HttpStatus.OK,
                messageSourceHandler.getLocalMessage("message.deletePost", request,
                        "Post has been deleted"));
    }

    public PostDto editCarLogbookPost(long id, PostCreationDto postCreationDto) {
        CarLogPost carLogbookPost = getCarLogPostById(id);
        carLogbookPost.setTopic(postCreationDto.topic());
        carLogbookPost.setDescription(postCreationDto.description());
        carLogbookPost.setEdited(true);
        CarLogPost editedCarLogbookPost = carLogBookPostRepository.save(carLogbookPost);
        log.info("Car Logbook Post with ID: {} has been edited", id);

        return mapPostToDto(editedCarLogbookPost);
    }

    private CarLogPost getCarLogPostById(long id) {
        Optional<CarLogPost> carLogPost = carLogBookPostRepository.findById(id);
        if (carLogPost.isEmpty()) {
            log.warn("Car LogBook Post with ID: {} not found", id);
            throw new ResourceNotFoundException("Car LogBook Post with id =" + id + " not found");
        }
        return carLogPost.get();
    }

    private ClubPost getClubPostById(long id) {
        Optional<ClubPost> clubPost = carClubPostRepository.findById(id);
        if (clubPost.isEmpty()) {
            log.warn("Car Club Post with ID: {} not found", id);
            throw new ResourceNotFoundException("Car Club Post with id =" + id + " not found");
        }
        return clubPost.get();
    }

    private CarClub getCarClubById(Long carClubId) {
        log.info("Getting Car Club with id: {}", carClubId);
        Optional<CarClub> carClub = carClubRepository.findById(carClubId);
        if (carClub.isEmpty()) {
            throw new ResourceNotFoundException("Car Logbook was not found");
        }
        return carClub.get();
    }

    public CarLogbook getCarLogbookById(Long carLogbookId) {
        log.info("Getting Car Logbook with id: {}", carLogbookId);
        Optional<CarLogbook> carLogbook = carLogbookRepository.findById(carLogbookId);
        if (carLogbook.isEmpty()) {
            throw new ResourceNotFoundException("Car Logbook was not found");
        }
        return carLogbook.get();
    }

    public CarLogbook getCarLogbookByCarId(Long carId) {
        log.info("Getting Car Logbook by car id: {}", carId);
        Optional<CarLogbook> carLogbook = carLogbookRepository.findByCar_Id(carId);
        if (carLogbook.isEmpty()) {
            throw new ResourceNotFoundException("Car Logbook was not found");
        }
        return carLogbook.get();
    }

    private PostDto mapPostToDto(Post post) {
        return new PostDto(post.getId(), post.getTopic(), post.getDescription(),
                post.getCreatedTime(), post.isEdited());
    }
}

