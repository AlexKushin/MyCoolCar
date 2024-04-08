package com.mycoolcar.services;

import com.mycoolcar.dtos.UserCreationDto;
import com.mycoolcar.entities.Role;
import com.mycoolcar.entities.User;
import com.mycoolcar.entities.VerificationToken;
import com.mycoolcar.exceptions.UserAlreadyExistException;
import com.mycoolcar.exceptions.UserNotFoundException;
import com.mycoolcar.repositories.RoleRepository;
import com.mycoolcar.repositories.UserRepository;
import com.mycoolcar.repositories.VerificationTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest  {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterNewUserAccount_Success() throws UserAlreadyExistException {
        // Mocking behavior
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(roleRepository.findByRoleName(UserService.ROLE_USER)).thenReturn(new Role());
        when(userRepository.save(any(User.class))).thenReturn(new User());

        // Call the method under test
        UserCreationDto userCreationDto = new UserCreationDto("John","Doe", "password", "password", "john@example.com" );
        Optional<User> result = userService.registerNewUserAccount(userCreationDto);

        // Verify the result
        assertTrue(result.isPresent());
    }

    @Test
    void testRegisterNewUserAccount_UserAlreadyExists() {
        // Mocking behavior
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        // Call the method under test and verify the exception
        assertThrows(UserAlreadyExistException.class, () -> {
            UserCreationDto userCreationDto = new UserCreationDto("John","Doe", "password", "password", "john@example.com" );
            userService.registerNewUserAccount(userCreationDto);
        });
    }

    @Test
    void testLoadUserByUsername_Success() {
        // Mocking behavior
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        // Call the method under test
        assertDoesNotThrow(() -> userService.loadUserByUsername("john@example.com"));
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Mocking behavior
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Call the method under test and verify the exception
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("john@example.com"));
    }

    @Test
    void testLoadUserById_Success() {
        long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.loadUserById(userId));
    }

    @Test
    void testLoadUserById_UserNotFound() {
        long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.loadUserById(userId));
        assertEquals("User with id =" + userId + " not found", exception.getMessage());
    }

    @Test
    void testSaveUser_Success() {
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        Optional<User> result = userService.save(user);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testGetUserByVerificationToken_Success() {
        String verificationToken = "token123";
        User user = new User();
        VerificationToken verificationTokenEntity = new VerificationToken(verificationToken, user);
        when(verificationTokenRepository.findByToken(verificationToken)).thenReturn(verificationTokenEntity);

        Optional<User> result = userService.getUserByVerificationToken(verificationToken);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testSaveRegisteredUser_Success() {
        User user = new User();

        userService.saveRegisteredUser(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testCreateVerificationTokenForUser_Success() {
        User user = new User();
        String token = "token123";
        VerificationToken verificationToken = new VerificationToken(token, user);

        userService.createVerificationTokenForUser(user, token);

        verify(verificationTokenRepository, times(1)).save(verificationToken);
    }

    @Test
    void testGetVerificationToken_Success() {
        String verificationToken = "token123";
        VerificationToken expectedToken = new VerificationToken(verificationToken, new User());
        when(verificationTokenRepository.findByToken(verificationToken)).thenReturn(expectedToken);

        VerificationToken result = userService.getVerificationToken(verificationToken);

        assertNotNull(result);
        assertEquals(expectedToken, result);
    }

    @Test
    void testDeleteVerificationToken_Success() {
        String token = "token123";
        VerificationToken verToken = new VerificationToken(token, new User());
        when(verificationTokenRepository.findByToken(token)).thenReturn(verToken);

        userService.deleteVerificationToken(token);

        verify(verificationTokenRepository, times(1)).delete(verToken);
    }

    @Test
    void testValidatePasswordResetToken_InvalidToken() {
        String invalidToken = "invalidToken";
        when(verificationTokenRepository.findByToken(invalidToken)).thenReturn(null);

        String result = userService.validatePasswordResetToken(invalidToken);

        assertEquals("invalidToken", result);
    }

    @Test
    void testValidatePasswordResetToken_ExpiredToken() {
        String expiredToken = "expiredToken";
        VerificationToken passToken = new VerificationToken(expiredToken, new User());
        passToken.setExpiryDate(LocalDateTime.now().minusDays(1));
        when(verificationTokenRepository.findByToken(expiredToken)).thenReturn(passToken);

        String result = userService.validatePasswordResetToken(expiredToken);

        assertEquals("expired", result);
    }

    @Test
    void testValidatePasswordResetToken_ValidToken() {
        String validToken = "validToken";
        VerificationToken passToken = new VerificationToken(validToken, new User());
        passToken.setExpiryDate(LocalDateTime.now().plusDays(1));
        when(verificationTokenRepository.findByToken(validToken)).thenReturn(passToken);

        String result = userService.validatePasswordResetToken(validToken);

        assertNull(result);
    }

    @Test
    void testBanUser_Success() {
        long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setBan(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        Optional<User> result = userService.banUser(userId);

        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
        assertTrue(result.get().isBan());
    }

    @Test
    void testBanUser_UserNotFound() {
        long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.banUser(userId));
    }

    @Test
    void testDeleteUser_Success() {
        long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.deleteUser(userId));
    }

    @Test
    void testDeleteUser_UserNotFound() {
        long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId));
    }

    @Test
    void testChangeUserPassword_Success() {
        User user = new User();
        String password = "newPassword";

        userService.changeUserPassword(user, password);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetUserByEmail_Success() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
    }

    @Test
    void testGetUserByEmail_UserNotFound() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByEmail(email);

        assertFalse(result.isPresent());
    }

    @Test
    void testGetByUsername_Success() {
        String username = "testuser";
        User user = new User();
        user.setFirstName(username);
        when(userRepository.findUserByFirstName(username)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(username, result.get().getFirstName());
    }

    @Test
    void testGetByUsername_UserNotFound() {
        String username = "testuser";
        when(userRepository.findUserByFirstName(username)).thenReturn(Optional.empty());

        Optional<User> result = userService.getByUsername(username);

        assertFalse(result.isPresent());
    }
}