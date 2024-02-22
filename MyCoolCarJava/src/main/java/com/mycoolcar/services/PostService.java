package com.mycoolcar.services;

import com.mycoolcar.entities.*;
import com.mycoolcar.repositories.CarLogPostRepository;
import com.mycoolcar.repositories.ClubPostRepository;
import com.mycoolcar.repositories.CarLogbookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final CarLogPostRepository carLogPostRepository;
    private final ClubPostRepository clubPostRepository;
    private final CarLogbookRepository carLogbookRepository;

    @Autowired
    public PostService(CarLogPostRepository carLogPostRepository, ClubPostRepository clubPostRepository,
                       CarLogbookRepository carLogbookRepository) {
        this.carLogPostRepository = carLogPostRepository;
        this.clubPostRepository = clubPostRepository;
        this.carLogbookRepository = carLogbookRepository;
    }

    public Post post(CarLogPost carLogPost) {
        return carLogPostRepository.save(carLogPost);
    }

    public Post post(ClubPost clubPost) {
        return clubPostRepository.save(clubPost);
    }

    public List<Post> getNewPosts(User user) {
        List<CarLogbook> carLogbooks = carLogbookRepository.findAllByCarIn(user.getSubscribedCars());
        List<CarLogPost> carLogPosts = carLogPostRepository.findAllByCarLogbookInOrderByCreatedTime(carLogbooks);
        List<ClubPost> clubPosts = clubPostRepository.findAllByCarClubInOrderByCreatedTime(user.getUserClubs());
        List<Post> news = new ArrayList<>();
        news.addAll(carLogPosts);
        news.addAll(clubPosts);
        return news;
    }
}
