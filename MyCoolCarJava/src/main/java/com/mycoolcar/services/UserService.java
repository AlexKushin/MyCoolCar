package com.mycoolcar.services;

import com.mycoolcar.dtos.UserCreationDto;
import com.mycoolcar.entities.User;
import com.mycoolcar.entities.Role;
import com.mycoolcar.exceptions.PersonFoundException;
import com.mycoolcar.repositories.UserRepository;
import com.mycoolcar.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
    /*@Bean
    PasswordEncoder bcryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }*/

    public void initRolesAndUsers() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Role adminRole = new Role();
        adminRole.setRoleName(ROLE_ADMIN);
        adminRole.setRoleDescription("admin role");
      //  adminRole.setId(1L);
        roleRepository.save(adminRole);
        Role userRole = new Role();
        userRole.setRoleName(ROLE_USER);
        userRole.setRoleDescription("user role");
      //  userRole.setId(2L);
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

    public Optional<User> get(String email) {
        return userRepository.findByEmail(email);
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
        personRoles.add(roleRepository.findByRoleName(ROLE_USER));
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
}
