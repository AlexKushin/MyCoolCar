package com.mycoolcar.services;

import com.mycoolcar.entities.*;
import com.mycoolcar.exceptions.ResourceNotFoundException;
import com.mycoolcar.repositories.CarClubPostRepository;
import com.mycoolcar.repositories.CarLogBookPostRepository;
import com.mycoolcar.repositories.CarLogbookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
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
        return carLogBookPostRepository.save(carLogPost);
    }

    public Post post(ClubPost clubPost) {
        return carClubPostRepository.save(clubPost);
    }

    public List<Post> getNewPosts(User user) {
        List<CarLogbook> carLogbooks = carLogbookRepository.findAllByCarIn(user.getSubscribedCars());
        return Stream.concat(
                        carLogBookPostRepository.findAllByCarLogbookInOrderByCreatedTime(carLogbooks).stream(),
                        carClubPostRepository.findAllByCarClubInOrderByCreatedTime(user.getUserClubs()).stream())
                .toList();
    }

    public void deleteCarClubPost(long id) {
        Optional<ClubPost> clubPostToDeleteOp = carClubPostRepository.findById(id);
        if (clubPostToDeleteOp.isEmpty()) {
            throw new ResourceNotFoundException("Car Club Post with id =" + id + " not found");
        }
        ClubPost clubPost = clubPostToDeleteOp.get();
        carClubPostRepository.delete(clubPost);
    }

    public void deleteCarLogPost(long id) {
        Optional<CarLogPost> carLogPostToDeleteOp = carLogBookPostRepository.findById(id);
        if (carLogPostToDeleteOp.isEmpty()) {
            throw new ResourceNotFoundException("Car LogBook Post with id =" + id + " not found");
        }
        CarLogPost carLogPost = carLogPostToDeleteOp.get();
        carLogBookPostRepository.delete(carLogPost);
    }
}

