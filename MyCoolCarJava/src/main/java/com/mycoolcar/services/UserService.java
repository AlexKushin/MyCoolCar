package com.mycoolcar.services;

import com.mycoolcar.dtos.UserCreationDto;
import com.mycoolcar.dtos.UserDto;
import com.mycoolcar.entities.Role;
import com.mycoolcar.entities.User;
import com.mycoolcar.entities.VerificationToken;
import com.mycoolcar.exceptions.UserAlreadyExistException;
import com.mycoolcar.exceptions.UserNotFoundException;
import com.mycoolcar.repositories.RoleRepository;
import com.mycoolcar.repositories.UserRepository;
import com.mycoolcar.repositories.VerificationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class UserService implements UserDetailsService, IUserService {
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;

    private static final String USER_ID = "User with id =";
    private static final String NOT_FOUND = " not found";
    private static final String USER_ID_NOT_FOUND = "User not found with ID: {}";


    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       VerificationTokenRepository verificationTokenRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    public void initRolesAndUsers() {
        log.info("Initializing roles and users");
        Role adminRole = new Role();
        adminRole.setRoleName(ROLE_ADMIN);
        adminRole.setRoleDescription("admin role");
        roleRepository.save(adminRole);
        log.info("Admin role created");

        Role userRole = new Role();
        userRole.setRoleName(ROLE_USER);
        userRole.setRoleDescription("user role");
        roleRepository.save(userRole);
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

    public Optional<User> getUserByEmail(String email) {
        log.info("Getting user by email: {}", email);
        return userRepository.findByEmail(email);
    }

    public UserDto getUserDtoByEmail(String email) {
        log.info("Getting user DTO by email: {}", email);
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            log.warn("User not found with email: {}", email);
            throw new UsernameNotFoundException("User was not found");
        }
        User user = userOptional.get();
        return new UserDto(user.getId(), user.isBan(), user.getFirstName(),
                user.getLastName(), user.getEmail(), user.isEnabled(), user.getRoles(), user.getUserCars());
    }

    public Optional<User> getByUsername(String username) {
        log.info("Getting user by username: {}", username);
        return userRepository.findUserByFirstName(username);
    }

    public Optional<User> getUserById(long userId) {
        log.info("Getting user by ID: {}", userId);
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> registerNewUserAccount(UserCreationDto userCreationDto) throws UserAlreadyExistException {
        log.info("Registering new user account with email: {}", userCreationDto.email());
        userRepository.findByEmail(userCreationDto.email()).ifPresent(user -> {
            log.warn("User with email: {} already exists", user.getEmail());
            throw new UserAlreadyExistException("User with email: " + user.getEmail() + " already exists");
        });
        userRepository.findUserByFirstName(userCreationDto.firstName()).ifPresent(user -> {
            log.warn("User with name: {} already exists", user.getFirstName());
            throw new UserAlreadyExistException("User with name: " + user.getFirstName() + " already exists");
        });
        User newUser = new User();
        newUser.setFirstName(userCreationDto.firstName());
        newUser.setLastName(userCreationDto.lastName());
        newUser.setEmail(userCreationDto.email());
        newUser.setPassword(passwordEncoder.encode(userCreationDto.password()));
        Set<Role> personRoles = newUser.getRoles();
        Role role = roleRepository.findByRoleName(ROLE_USER);
        personRoles.add(role);
        newUser.setRoles(personRoles);
        log.info("User registered successfully with email: {}", userCreationDto.email());
        return Optional.of(userRepository.save(newUser));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Loading user by username (email): {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User not found with email: {}", email);
                    return new UsernameNotFoundException("User was not found");
                });
    }

    public UserDetails loadUserById(long id) {
        log.info("Loading user by ID: {}", id);
        return userRepository.findById(id).orElseThrow(() -> {
            log.warn(USER_ID_NOT_FOUND, id);
            return new UserNotFoundException(USER_ID + id + NOT_FOUND);
        });
    }

    public Optional<User> save(User user) {
        log.info("Saving user with email: {}", user.getEmail());
        return Optional.of(userRepository.save(user));
    }


    @Override
    public Optional<User> getUserByVerificationToken(String verificationToken) {
        log.info("Getting user by verification token");
        return Optional.of(verificationTokenRepository.findByToken(verificationToken).getUser());
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
        return verificationTokenRepository.findByToken(verificationToken);
    }

    @Override
    public void deleteVerificationToken(String token) {
        log.info("Deleting verification token");
        VerificationToken verToken = getVerificationToken(token);
        verificationTokenRepository.delete(verToken);
    }

    @Override
    public String validatePasswordResetToken(String token) {
        log.info("Validating password reset token");
        final VerificationToken passToken = verificationTokenRepository.findByToken(token);
        if (passToken == null) {
            log.warn("Invalid token: {}", token);
            return "invalidToken";
        }
        return isTokenExpired(passToken) ? "expired" : null;
    }

    @Override
    public Optional<User> banUser(long id) {
        log.info("Banning/unbanning user with ID: {}", id);
        Optional<User> userToBanOp = getUserById(id);
        if (userToBanOp.isEmpty()) {
            log.warn(USER_ID_NOT_FOUND, id);
            throw new UserNotFoundException(USER_ID + id + NOT_FOUND);
        }
        User user = userToBanOp.get();
        user.setBan(!user.isBan());
        log.info("User with ID: {} banned/unbanned successfully", id);
        return save(user);
    }

    @Override
    public void deleteUser(long id) {
        log.info("Deleting user with ID: {}", id);
        Optional<User> userToDeleteOp = getUserById(id);
        if (userToDeleteOp.isEmpty()) {
            log.warn(USER_ID_NOT_FOUND, id);
            throw new UserNotFoundException(USER_ID + id + NOT_FOUND);
        }
        User user = userToDeleteOp.get();
        userRepository.delete(user);
        log.info("User with ID: {} deleted successfully", id);
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
}
