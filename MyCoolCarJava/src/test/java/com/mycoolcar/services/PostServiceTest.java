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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PostServiceTest {
    @Mock
    private CarLogBookPostRepository carLogBookPostRepository;

    @Mock
    private CarClubPostRepository carClubPostRepository;

    @Mock
    private CarLogbookRepository carLogbookRepository;

    @Mock
    private CarClubRepository carClubRepository;

    @Mock
    private MessageSourceHandler messageSourceHandler;

    @InjectMocks
    private PostService postService;

    private CarLogbook carLogbook;
    private CarClub carClub;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        carLogbook = new CarLogbook(); // Initialize dummy CarLogbook
        carClub = new CarClub();       // Initialize dummy CarClub
    }

    @Test
    void postCarLogbookPost_ShouldCreateNewLogbookPost() {
        // Arrange
        Long carLogbookId = 1L;
        PostCreationDto postCreationDto = new PostCreationDto("Topic", "Description");
        CarLogPost newCarLogPost = new CarLogPost();
        newCarLogPost.setId(1L);
        newCarLogPost.setTopic("Topic");
        newCarLogPost.setDescription("Description");
        newCarLogPost.setCreatedTime(LocalDateTime.now());
        when(carLogbookRepository.findById(carLogbookId)).thenReturn(Optional.of(carLogbook));
        when(carLogBookPostRepository.save(any(CarLogPost.class))).thenReturn(newCarLogPost);

        // Act
        PostDto result = postService.postCarLogbookPost(carLogbookId, postCreationDto);

        // Assert
        assertNotNull(result);
        assertEquals("Topic", result.topic());
        verify(carLogbookRepository, times(1)).findById(carLogbookId);
        verify(carLogBookPostRepository, times(1)).save(any(CarLogPost.class));
    }

    @Test
    void postCarLogbookPost_ShouldThrowResourceNotFound_WhenCarLogbookNotFound() {
        // Arrange
        Long carLogbookId = 1L;
        PostCreationDto postCreationDto = new PostCreationDto("Topic", "Description");
        when(carLogbookRepository.findById(carLogbookId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> postService.postCarLogbookPost(carLogbookId, postCreationDto));
        verify(carLogbookRepository, times(1)).findById(carLogbookId);
    }

    @Test
    void postCarClubPost_ShouldCreateNewCarClubPost() {
        // Arrange
        Long carClubId = 1L;
        PostCreationDto postCreationDto = new PostCreationDto("Club Topic", "Club Description");
        ClubPost newCarClubPost = new ClubPost();
        newCarClubPost.setId(1L);
        newCarClubPost.setTopic("Club Topic");
        newCarClubPost.setDescription("Club Description");
        newCarClubPost.setCreatedTime(LocalDateTime.now());
        when(carClubRepository.findById(carClubId)).thenReturn(Optional.of(carClub));
        when(carClubPostRepository.save(any(ClubPost.class))).thenReturn(newCarClubPost);

        // Act
        PostDto result = postService.postCarClubPost(carClubId, postCreationDto);

        // Assert
        assertNotNull(result);
        assertEquals("Club Topic", result.topic());
        verify(carClubRepository, times(1)).findById(carClubId);
        verify(carClubPostRepository, times(1)).save(any(ClubPost.class));
    }

    @Test
    void deleteCarLogPost_ShouldDeleteCarLogPostSuccessfully() {
        // Arrange
        long postId = 1L;
        CarLogPost carLogPost = new CarLogPost();
        carLogPost.setId(postId);
        when(carLogBookPostRepository.findById(postId)).thenReturn(Optional.of(carLogPost));
        WebRequest request = mock(WebRequest.class);
        when(messageSourceHandler.getLocalMessage(anyString(), eq(request), anyString())).thenReturn("Post deleted");

        // Act
        ApiResponse response = postService.deleteCarLogPost(postId, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.statusCode());
        assertEquals("Post deleted", response.message());
        verify(carLogBookPostRepository, times(1)).delete(carLogPost);
    }

    @Test
    void deleteCarClubPost_ShouldDeleteCarClubPostSuccessfully() {
        // Arrange
        long postId = 1L;
        ClubPost clubPost = new ClubPost();
        clubPost.setId(postId);
        when(carClubPostRepository.findById(postId)).thenReturn(Optional.of(clubPost));
        WebRequest request = mock(WebRequest.class);
        when(messageSourceHandler.getLocalMessage(anyString(), eq(request), anyString())).thenReturn("Post deleted");

        // Act
        ApiResponse response = postService.deleteCarClubPost(postId, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.statusCode());
        assertEquals("Post deleted", response.message());
        verify(carClubPostRepository, times(1)).delete(clubPost);
    }

    @Test
    void editCarLogbookPost_ShouldEditCarLogbookPostSuccessfully() {
        // Arrange
        long postId = 1L;
        PostCreationDto postCreationDto = new PostCreationDto("New Topic", "New Description");
        CarLogPost carLogPost = new CarLogPost();
        carLogPost.setId(postId);
        carLogPost.setTopic("Old Topic");
        carLogPost.setDescription("Old Description");
        when(carLogBookPostRepository.findById(postId)).thenReturn(Optional.of(carLogPost));
        when(carLogBookPostRepository.save(any(CarLogPost.class))).thenReturn(carLogPost);

        // Act
        PostDto result = postService.editCarLogbookPost(postId, postCreationDto);

        // Assert
        assertNotNull(result);
        assertEquals("New Topic", result.topic());
        assertEquals("New Description", result.description());
        assertTrue(result.edited());
        verify(carLogBookPostRepository, times(1)).findById(postId);
        verify(carLogBookPostRepository, times(1)).save(any(CarLogPost.class));
    }

    @Test
    void getCarLogPostById_ShouldThrowException_WhenCarLogPostNotFound() {
        // Arrange
        long postId = 1L;
        when(carLogBookPostRepository.findById(postId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> postService.editCarLogbookPost(postId, new PostCreationDto("Topic", "Description")));
        verify(carLogBookPostRepository, times(1)).findById(postId);
    }

}