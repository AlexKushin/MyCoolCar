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
        Role adminRole = new Role();
        adminRole.setRoleName(ROLE_ADMIN);
        adminRole.setRoleDescription("admin role");
        roleRepository.save(adminRole);
        Role userRole = new Role();
        userRole.setRoleName(ROLE_USER);
        userRole.setRoleDescription("user role");
        roleRepository.save(userRole);


        User admin = new User();
        admin.setFirstName("admin");
        admin.setLastName("admin");
        admin.setEmail("admin@gmail.com");
        admin.setPassword(passwordEncoder.encode("password"));
        Set<Role> personRoles = new HashSet<>();
        personRoles.add(adminRole);
        admin.setRoles(personRoles);
        userRepository.save(admin);

        User user = new User();
        user.setFirstName("user");
        user.setLastName("user");
        user.setEmail("user@gmail.com");
        user.setPassword(passwordEncoder.encode("password"));
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        user.setRoles(userRoles);
        userRepository.save(user);
    }
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserDto getUserDtoByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new UsernameNotFoundException("user was not found");
        }
        User user = userOptional.get();
        return new UserDto(user.getId(),user.isBan(), user.getFirstName(),
                user.getLastName(), user.getEmail(), user.isEnabled(), user.getRoles());
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findUserByFirstName(username);
    }

    public Optional<User> getUserById(long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> registerNewUserAccount(UserCreationDto userCreationDto) throws UserAlreadyExistException {
        userRepository.findByEmail(userCreationDto.email()).ifPresent(user -> {
            throw new UserAlreadyExistException("User with email: " + user.getEmail() + " is already exists");
        });
        userRepository.findUserByFirstName(userCreationDto.firstName()).ifPresent(user -> {
            throw new UserAlreadyExistException("User with name: " + user.getFirstName() + " is already exists");
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
        return Optional.of(userRepository.save(newUser));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("user was not found"));
    }

    public UserDetails loadUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id =" + id + " not found"));
    }

    public Optional<User> save(User user) {
        return Optional.of(userRepository.save(user));
    }


    @Override
    public Optional<User> getUserByVerificationToken(String verificationToken) {
        return Optional.of(verificationTokenRepository.findByToken(verificationToken).getUser());
    }

    @Override
    public void saveRegisteredUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void createVerificationTokenForUser(User user, String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        verificationTokenRepository.save(myToken);
    }

    @Override
    public VerificationToken getVerificationToken(String verificationToken) {
        return verificationTokenRepository.findByToken(verificationToken);
    }

    @Override
    public void deleteVerificationToken(String token) {
        VerificationToken verToken = getVerificationToken(token);
        verificationTokenRepository.delete(verToken);
    }

    @Override
    public String validatePasswordResetToken(String token) {
        final VerificationToken passToken = verificationTokenRepository.findByToken(token);
        if (passToken == null) {
            return "invalidToken";
        }
        return isTokenExpired(passToken) ? "expired" : null;
    }

    @Override
    public Optional<User> banUser(long id) {
        Optional<User> userToBanOp =  getUserById(id);
        if(userToBanOp.isEmpty()){
            throw  new UserNotFoundException("User with id =" + id + " not found");
        }
        User user = userToBanOp.get();
        user.setBan(!user.isBan());
        return save(user);
    }

    @Override
    public void deleteUser(long id) {
        Optional<User> userToDeleteOp =  getUserById(id);
        if(userToDeleteOp.isEmpty()){
            throw  new UserNotFoundException("User with id =" + id + " not found");
        }
        User user = userToDeleteOp.get();
        userRepository.delete(user);
    }

    private boolean isTokenExpired(VerificationToken passToken) {
        return passToken.getExpiryDate().isBefore(LocalDateTime.now());
    }

    public void changeUserPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }
}
