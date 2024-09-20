package com.mycoolcar.services;

import com.mycoolcar.dtos.NewPasswordDto;
import com.mycoolcar.dtos.UserCreationDto;
import com.mycoolcar.dtos.UserDto;
import com.mycoolcar.entities.Role;
import com.mycoolcar.entities.User;
import com.mycoolcar.entities.VerificationToken;
import com.mycoolcar.exceptions.ResourceNotFoundException;
import com.mycoolcar.exceptions.UserAlreadyExistException;
import com.mycoolcar.exceptions.UserNotFoundException;
import com.mycoolcar.registration.OnRegistrationCompleteEvent;
import com.mycoolcar.registration.OnResetPasswordEvent;
import com.mycoolcar.repositories.UserRepository;
import com.mycoolcar.repositories.VerificationTokenRepository;
import com.mycoolcar.util.ApiResponse;
import com.mycoolcar.util.MessageSourceHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class UserService implements UserDetailsService, IUserService {

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";

    private static final String USER_ID = "User with id =";
    private static final String NOT_FOUND = " not found";
    private static final String USER_ID_NOT_FOUND = "User not found with ID: {}";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final MessageSourceHandler messageSourceHandler;
    private final RoleService roleService;


    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       VerificationTokenRepository verificationTokenRepository,
                       ApplicationEventPublisher eventPublisher,
                       MessageSourceHandler messageSourceHandler,
                       RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepository = verificationTokenRepository;
        this.eventPublisher = eventPublisher;
        this.messageSourceHandler = messageSourceHandler;
        this.roleService = roleService;
    }

    public void initRolesAndUsers() {
        log.info("Initializing roles and users");
        Role adminRole = new Role();
        adminRole.setRoleName(ROLE_ADMIN);
        adminRole.setRoleDescription("admin role");
        roleService.createNewRole(adminRole);
        log.info("Admin role created");

        Role userRole = new Role();
        userRole.setRoleName(ROLE_USER);
        userRole.setRoleDescription("user role");
        roleService.createNewRole(userRole);
        log.info("User role created");

        User admin = new User();
        admin.setFirstName("admin");
        admin.setLastName("admin");
        admin.setEmail("admin@gmail.com");
        admin.setPassword(passwordEncoder.encode("password"));
        Set<Role> personRoles = new HashSet<>();
        personRoles.add(adminRole);
        admin.setRoles(personRoles);
        userRepository.save(admin);
        log.info("Admin user created");

        User user = new User();
        user.setFirstName("user");
        user.setLastName("user");
        user.setEmail("user@gmail.com");
        user.setPassword(passwordEncoder.encode("password"));
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        user.setRoles(userRoles);
        userRepository.save(user);
        log.info("User created");
    }

    public User getUserByEmail(String email) {
        log.info("Getting user by email: {}", email);
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with email " + email + " not found");
        }
        return user.get();
    }

    public User getUserByFirstName(String firstName) {
        log.info("Getting user by first name: {}", firstName);
        Optional<User> user = userRepository.findUserByFirstName(firstName);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with email " + firstName + " not found");
        }
        return user.get();
    }

    public UserDto getUserDtoByEmail(String email) {
        log.info("Getting user DTO by email: {}", email);
        User user = getUserByEmail(email);
        return mapUserToDto(user);
    }

    public Optional<User> getByUsername(String username) {
        log.info("Getting user by username: {}", username);
        return userRepository.findUserByFirstName(username);
    }

    public User getUserById(long userId) {
        log.info("Getting user by ID: {}", userId);
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            log.warn(USER_ID_NOT_FOUND, userId);
            throw new UserNotFoundException(USER_ID + user + NOT_FOUND);
        }
        return user.get();
    }

    @Override
    public UserDto registerNewUserAccount(UserCreationDto userCreationDto, WebRequest request) throws UserAlreadyExistException {
        log.info("Registering new user account with email: {}", userCreationDto.email());
        //todo write user exist check
        getUserByEmail(userCreationDto.email());
        getUserByFirstName(userCreationDto.firstName());
        User registeredUser = saveUser(userCreationDto);

        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registeredUser, request));
        return mapUserToDto(registeredUser);
    }

    private User saveUser(UserCreationDto userCreationDto) {
        log.info("Saving user with email: {}", userCreationDto.email());
        User newUser = new User();
        newUser.setFirstName(userCreationDto.firstName());
        newUser.setLastName(userCreationDto.lastName());
        newUser.setEmail(userCreationDto.email());
        newUser.setPassword(passwordEncoder.encode(userCreationDto.password()));
        Set<Role> personRoles = newUser.getRoles();
        Role role = roleService.findByRoleName(ROLE_USER);
        personRoles.add(role);
        newUser.setRoles(personRoles);
        log.info("User registered successfully with email: {}", userCreationDto.email());
        return userRepository.save(newUser);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Loading user by username (email): {}", email);
        return getUserByEmail(email);
    }

    public UserDetails loadUserById(long id) {
        log.info("Loading user by ID: {}", id);
        return getUserById(id);
    }

    @Override
    public User getUserByVerificationToken(String verificationTokenStr) {
        log.info("Getting user by verification token");
        VerificationToken token = getVerificationToken(verificationTokenStr);
        return token.getUser();
    }

    @Override
    public void saveRegisteredUser(User user) {
        log.info("Saving registered user with email: {}", user.getEmail());
        userRepository.save(user);
    }

    @Override
    public void createVerificationTokenForUser(User user, String token) {
        log.info("Creating verification token for user with email: {}", user.getEmail());
        final VerificationToken myToken = new VerificationToken(token, user);
        verificationTokenRepository.save(myToken);
    }

    @Override
    public VerificationToken getVerificationToken(String verificationToken) {
        log.info("Getting verification token");
        Optional<VerificationToken> verToken = verificationTokenRepository.findByToken(verificationToken);
        if (verToken.isEmpty()) {
            throw new ResourceNotFoundException("Token was not found");
        }
        return verToken.get();
    }

    @Override
    public void deleteVerificationToken(String token) {
        log.info("Deleting verification token");
        VerificationToken verToken = getVerificationToken(token);
        verificationTokenRepository.delete(verToken);
    }

    public ApiResponse confirmRegistration(WebRequest request, String token) {
        String result = validatePasswordResetToken(token);
        if (result != null) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, messageSourceHandler
                    .getLocalMessage("auth.message." + result, request, "Invalid token"));
        }
        User user = getUserByVerificationToken(token);
        user.setEnabled(true);
        saveRegisteredUser(user);
        deleteVerificationToken(token);
        String message = messageSourceHandler
                .getLocalMessage("auth.message.confirm", request, "registration confirmed");
        return new ApiResponse(HttpStatus.OK, message);
    }

    public ApiResponse resetPassword(WebRequest request, final String userEmail) {
        User user = getUserByEmail(userEmail);
        eventPublisher.publishEvent(new OnResetPasswordEvent(user, request));
        return new ApiResponse(HttpStatus.OK,
                messageSourceHandler.getLocalMessage("message.resetPasswordEmail", request,
                        "You should receive and Password Reset Email shortly"));
    }

    public ApiResponse savePassword(WebRequest request, NewPasswordDto passwordDto) {
        String result = validatePasswordResetToken(passwordDto.token());
        if (result != null) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, messageSourceHandler.getLocalMessage(
                    "auth.message." + result, request, "Invalid token"));
        }
        User user = getUserByVerificationToken(passwordDto.token());
        changeUserPassword(user, passwordDto.password());
        return new ApiResponse(HttpStatus.OK, messageSourceHandler.getLocalMessage(
                "message.resetPasswordSuc", request, "Password reset successfully"));
    }

    @Override
    public String validatePasswordResetToken(String token) {
        log.info("Validating password reset token");
        final Optional<VerificationToken> passToken = verificationTokenRepository.findByToken(token);
        if (passToken.isEmpty()) {
            log.warn("Invalid token: {}", token);
            return "invalidToken";
        }
        return isTokenExpired(passToken.get()) ? "expired" : null;
    }

    @Override
    public UserDto banUser(long id) {
        log.info("Banning/unbanning user with ID: {}", id);
        User user = getUserById(id);
        user.setBan(!user.isBan());
        User bannedUser = userRepository.save(user);
        log.info("User with ID: {} banned/unbanned successfully", id);
        return mapUserToDto(bannedUser);
    }

    @Override
    public ApiResponse deleteUser(long id, WebRequest request) {
        log.info("Deleting user with ID: {}", id);
        User user = getUserById(id);
        userRepository.delete(user);
        log.info("User with ID: {} deleted successfully", id);
        return new ApiResponse(HttpStatus.OK,
                messageSourceHandler.getLocalMessage("message.deleteUser", request,
                        "User has been deleted successfully"));
    }

    private boolean isTokenExpired(VerificationToken passToken) {
        return passToken.getExpiryDate().isBefore(LocalDateTime.now());
    }

    public void changeUserPassword(User user, String password) {
        log.info("Changing password for user with email: {}", user.getEmail());
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        log.info("Password changed successfully for user with email: {}", user.getEmail());
    }

    private UserDto mapUserToDto(User user) {
        return new UserDto(user.getId(), user.isBan(),
                user.getRegistered(), user.getFirstName(),
                user.getLastName(), user.getEmail(),
                user.isEnabled());
    }
}
