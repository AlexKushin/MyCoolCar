package com.mycoolcar.services;

import com.mycoolcar.dtos.CarLogPostDto;
import com.mycoolcar.entities.*;
import com.mycoolcar.exceptions.ResourceNotFoundException;
import com.mycoolcar.repositories.CarClubPostRepository;
import com.mycoolcar.repositories.CarLogBookPostRepository;
import com.mycoolcar.repositories.CarLogbookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Slf4j
public class PostService {

    private final CarLogBookPostRepository carLogBookPostRepository;
    private final CarClubPostRepository carClubPostRepository;
    private final CarLogbookRepository carLogbookRepository;

    @Autowired
    public PostService(CarLogBookPostRepository carLogBookPostRepository,
                       CarClubPostRepository carClubPostRepository,
                       CarLogbookRepository carLogbookRepository) {
        this.carLogBookPostRepository = carLogBookPostRepository;
        this.carClubPostRepository = carClubPostRepository;
        this.carLogbookRepository = carLogbookRepository;
    }

    public Post post(CarLogPost carLogPost) {
        log.info("Posting CarLogPost: {}", carLogPost);
        Post savedPost = carLogBookPostRepository.save(carLogPost);
        log.info("CarLogPost saved with ID: {}", savedPost.getId());
        return savedPost;
    }

    public Post post(ClubPost clubPost) {
        log.info("Posting ClubPost: {}", clubPost);
        Post savedPost = carClubPostRepository.save(clubPost);
        log.info("ClubPost saved with ID: {}", savedPost.getId());
        return savedPost;
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

    public void deleteCarClubPost(long id) {
        log.info("Deleting CarClubPost with ID: {}", id);
        Optional<ClubPost> clubPostToDeleteOp = carClubPostRepository.findById(id);
        if (clubPostToDeleteOp.isEmpty()) {
            log.warn("Car Club Post with ID: {} not found", id);
            throw new ResourceNotFoundException("Car Club Post with id =" + id + " not found");
        }
        ClubPost clubPost = clubPostToDeleteOp.get();
        carClubPostRepository.delete(clubPost);
        log.info("Deleted CarClubPost with ID: {}", id);
    }

    public void deleteCarLogPost(long id) {
        log.info("Deleting CarLogPost with ID: {}", id);
        Optional<CarLogPost> carLogPostToDeleteOp = carLogBookPostRepository.findById(id);
        if (carLogPostToDeleteOp.isEmpty()) {
            log.warn("Car LogBook Post with ID: {} not found", id);
            throw new ResourceNotFoundException("Car LogBook Post with id =" + id + " not found");
        }
        CarLogPost carLogPost = carLogPostToDeleteOp.get();
        carLogBookPostRepository.delete(carLogPost);
        log.info("Deleted CarLogPost with ID: {}", id);
    }

    public Optional<CarLogPost> editCarLogbookPost(long id, CarLogPostDto carLogPostDto) {
        Optional<CarLogPost> carLogbookPostOp=carLogBookPostRepository.findById(id);
        if (carLogbookPostOp.isEmpty()) {
            log.warn("Car LogBook Post with ID: {} not found", id);
            throw new ResourceNotFoundException("Car LogBook Post with id =" + id + " not found");
        }
        CarLogPost carLogbookPost = carLogbookPostOp.get();
        carLogbookPost.setTopic(carLogPostDto.topic());
        carLogbookPost.setDescription(carLogPostDto.description());
        carLogbookPost.setEdited(true);
        carLogBookPostRepository.save(carLogbookPost);
        log.info("Car Logbook Post with ID: {} has been edited", id);

        return Optional.of(carLogbookPost);
    }
}

