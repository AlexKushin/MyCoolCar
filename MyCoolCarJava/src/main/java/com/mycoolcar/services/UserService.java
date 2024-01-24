package com.mycoolcar.services;

import com.mycoolcar.dtos.UserCreationDto;
import com.mycoolcar.entities.User;
import com.mycoolcar.entities.Role;
import com.mycoolcar.exceptions.PersonFoundException;
import com.mycoolcar.repositories.UserRepository;
import com.mycoolcar.repositories.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
@Slf4j
@Service
public class UserService  implements UserDetailsService {
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public void initRolesAndUsers() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
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
        admin.setPassword(encoder.encode("password"));
        Set<Role> personRoles = new HashSet<>();
        personRoles.add(adminRole);
        admin.setRoles(personRoles);
        userRepository.save(admin);

        User user = new User();
        user.setFirstName("user");
        user.setLastName("user");
        user.setEmail("user@gmail.com");
        user.setPassword(encoder.encode("password"));
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        user.setRoles(userRoles);
        userRepository.save(user);
    }

    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public Optional<User> getByUsername(String username) {
        return userRepository.findUserByFirstName(username);
    }

    public Optional<User> registerNewUser(UserCreationDto userCreationDto) {
        userRepository.findByEmail(userCreationDto.email()).ifPresent(person -> {
            throw new PersonFoundException("User with ID: " + person.getEmail() + " is already exists");
        });
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User newUser = new User();
        newUser.setFirstName(userCreationDto.firstName());
        newUser.setLastName(userCreationDto.lastName());
        newUser.setEmail(userCreationDto.email());
        newUser.setPassword(encoder.encode(userCreationDto.password()));
        Set<Role> personRoles = newUser.getRoles();
        Role role = roleRepository.findByRoleName(ROLE_USER);
        personRoles.add(role);
        newUser.setRoles(personRoles);
        return Optional.of(userRepository.save(newUser));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("user was not found")/*createUserIfNotExists()*/);
    }

    public UserDetails loadUserById(long id) {
        return userRepository.findById(id).orElseThrow(/*() -> new ResourceNotFoundException("User", "id", id)*/);
    }
    public User save(User user){
        return userRepository.save(user);
    }
}
