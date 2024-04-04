package com.mycoolcar.services;

import com.mycoolcar.entities.CarLogPost;
import com.mycoolcar.entities.ClubPost;
import com.mycoolcar.entities.Post;
import com.mycoolcar.entities.User;
import com.mycoolcar.exceptions.ResourceNotFoundException;
import com.mycoolcar.repositories.CarClubPostRepository;
import com.mycoolcar.repositories.CarLogBookPostRepository;
import com.mycoolcar.repositories.CarLogbookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PostServiceTest {
    @Mock
    private CarLogBookPostRepository carLogBookPostRepository;
    @Mock
    private CarClubPostRepository carClubPostRepository;
    @Mock
    private CarLogbookRepository carLogbookRepository;
    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPostCarLogPost() {
        CarLogPost carLogPost = new CarLogPost(); // Create a sample CarLogPost instance
        carLogPost.setTopic("topic");
        carLogPost.setDescription("description");
        when(carLogBookPostRepository.save(carLogPost)).thenReturn(carLogPost); // Mock repository behavior
        Post result = postService.post(carLogPost); // Call the method under test
        assertEquals(carLogPost, result); // Assert the result
        assertEquals("topic", result.getTopic());
        assertEquals("description", result.getDescription());
        verify(carLogBookPostRepository, times(1)).save(carLogPost);
    }

    @Test
    void testPostClubPost() {
        ClubPost clubPost = new ClubPost();// Create a sample ClubPost instance
        clubPost.setTopic("topic");
        clubPost.setDescription("description");
        when(carClubPostRepository.save(clubPost)).thenReturn(clubPost); // Mock repository behavior
        Post result = postService.post(clubPost); // Call the method under test
        assertEquals(clubPost, result); // Assert the result
        assertEquals("topic", result.getTopic());
        assertEquals("description", result.getDescription());
        verify(carClubPostRepository, times(1)).save(clubPost);
    }

    @Test
    void testGetNewPosts() {
        ClubPost clubPost = new ClubPost();// Create a sample ClubPost instance
        CarLogPost carLogPost = new CarLogPost(); // Create a sample CarLogPost instance

        List<ClubPost> clubPostList = new ArrayList<>();
        clubPostList.add(clubPost);
        List<CarLogPost>  carLogPostList = new ArrayList<>();
        carLogPostList.add(carLogPost);

        // Mock repository behavior
        when(carLogbookRepository.findAllByCarIn(any())).thenReturn(new ArrayList<>());
        when(carLogBookPostRepository.findAllByCarLogbookInOrderByCreatedTime(any())).thenReturn(carLogPostList);
        when(carClubPostRepository.findAllByCarClubInOrderByCreatedTime(any())).thenReturn(clubPostList);

        // Call the method under test
        List<Post> result = postService.getNewPosts(new User());

        // Assert the result
        assertEquals(2, result.size());

        //verify methods calls
        verify(carLogbookRepository, times(1)).findAllByCarIn(any());
        verify(carLogBookPostRepository, times(1)).findAllByCarLogbookInOrderByCreatedTime(any());
        verify(carClubPostRepository, times(1)).findAllByCarClubInOrderByCreatedTime(any());
    }

    @Test
    void testDeleteCarClubPost_Success() {
        long postId = 1L;
        ClubPost clubPost = new ClubPost();
        when(carClubPostRepository.findById(postId)).thenReturn(Optional.of(clubPost));

        postService.deleteCarClubPost(postId);

        verify(carClubPostRepository, times(1)).delete(clubPost);
    }

    @Test
    void testDeleteCarClubPost_PostNotFound() {
        long postId = 1L;
        when(carClubPostRepository.findById(postId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> postService.deleteCarClubPost(postId));
    }

    @Test
    void testDeleteCarLogPost_Success() {
        long postId = 1L;
        CarLogPost carLogPost = new CarLogPost();
        when(carLogBookPostRepository.findById(postId)).thenReturn(Optional.of(carLogPost));

        postService.deleteCarLogPost(postId);

        verify(carLogBookPostRepository, times(1)).delete(carLogPost);
    }

    @Test
    void testDeleteCarLogPost_PostNotFound() {
        long postId = 1L;
        when(carLogBookPostRepository.findById(postId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> postService.deleteCarLogPost(postId));
    }


}