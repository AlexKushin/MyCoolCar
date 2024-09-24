package com.mycoolcar.services;

import com.mycoolcar.dtos.UserCreationDto;
import com.mycoolcar.dtos.UserDto;
import com.mycoolcar.entities.User;
import com.mycoolcar.entities.VerificationToken;
import com.mycoolcar.exceptions.ResourceNotFoundException;
import com.mycoolcar.exceptions.UserAlreadyExistException;
import com.mycoolcar.exceptions.UserNotFoundException;
import com.mycoolcar.registration.OnRegistrationCompleteEvent;
import com.mycoolcar.repositories.UserRepository;
import com.mycoolcar.repositories.VerificationTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.WebRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserByEmail_UserExists_ReturnsUser() {
        // Arrange
        String email = "test@example.com";
        User mockUser = new User();
        mockUser.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        // Act
        User result = userService.getUserByEmail(email);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void getUserByEmail_UserDoesNotExist_ThrowsUserNotFoundException() {
        // Arrange
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userService.getUserByEmail(email));
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        // Arrange
        String email = "test@example.com";
        User mockUser = new User();
        mockUser.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        // Act
        UserDetails userDetails = userService.loadUserByUsername(email);

        // Assert
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void loadUserByUsername_UserDoesNotExist_ThrowsUsernameNotFoundException() {
        // Arrange
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(email));
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void registerNewUserAccount_UserAlreadyExists_ThrowsUserAlreadyExistException() {
        // Arrange
        String email = "test@example.com";
        UserCreationDto userCreationDto = new UserCreationDto("John", "Doe", "password", "password", email);
        WebRequest webRequest = mock(WebRequest.class);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User())); // User already exists

        // Act & Assert
        assertThrows(UserAlreadyExistException.class, () -> {
            userService.registerNewUserAccount(userCreationDto, webRequest);
        });

        // Verify that isUserExists was called and saveUser was not
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, never()).save(any(User.class)); // Ensure save is never called
        verify(eventPublisher, never()).publishEvent(any(OnRegistrationCompleteEvent.class));
    }

    @Test
    void registerNewUserAccount_SuccessfulRegistration_ReturnsUserDto() {
        // Arrange
        String email = "john@example.com";
        String encodedPassword = "encodedPassword";
        UserCreationDto userCreationDto = new UserCreationDto("John", "Doe", "password", "password", email);
        WebRequest webRequest = mock(WebRequest.class);
        User savedUser = new User();
        savedUser.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty()); // No user exists
        when(passwordEncoder.encode("password")).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        UserDto userDto = userService.registerNewUserAccount(userCreationDto, webRequest);

        // Assert
        assertNotNull(userDto);
        assertEquals(email, userDto.email());

        // Verify that isUserExists was called, user was saved, and event was published
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(1)).save(any(User.class));
        verify(eventPublisher, times(1)).publishEvent(any(OnRegistrationCompleteEvent.class));
    }

    @Test
    void getVerificationToken_TokenExists_ReturnsVerificationToken() {
        // Arrange
        String token = "valid-token";
        VerificationToken mockToken = new VerificationToken();
        when(verificationTokenRepository.findByToken(token)).thenReturn(Optional.of(mockToken));

        // Act
        VerificationToken result = userService.getVerificationToken(token);

        // Assert
        assertNotNull(result);
        verify(verificationTokenRepository, times(1)).findByToken(token);
    }

    @Test
    void getVerificationToken_TokenDoesNotExist_ThrowsResourceNotFoundException() {
        // Arrange
        String token = "invalid-token";

        when(verificationTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.getVerificationToken(token));
        verify(verificationTokenRepository, times(1)).findByToken(token);
    }

    @Test
    void banUser_UserExists_TogglesBanStatus() {
        // Arrange
        long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setBan(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        // Act
        UserDto result = userService.banUser(userId);

        // Assert
        assertNotNull(result);
        assertTrue(mockUser.isBan());  // Ban status should be toggled
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    void banUser_UserDoesNotExist_ThrowsUserNotFoundException() {
        // Arrange
        long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.banUser(userId));
        verify(userRepository, times(1)).findById(userId);
    }
}