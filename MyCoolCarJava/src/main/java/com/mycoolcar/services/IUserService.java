package com.mycoolcar.services;

import com.mycoolcar.dtos.UserCreationDto;
import com.mycoolcar.entities.User;
import com.mycoolcar.entities.VerificationToken;
import com.mycoolcar.exceptions.UserAlreadyExistException;

import java.util.Optional;

public interface IUserService {
    Optional<User> registerNewUserAccount(UserCreationDto userDto)
            throws UserAlreadyExistException;

    Optional <User> getUserByVerificationToken(String verificationToken);

    void saveRegisteredUser(User user);

    void createVerificationTokenForUser(User user, String token);

    VerificationToken getVerificationToken(String verificationToken);

    void deleteVerificationToken(String token);

    String validatePasswordResetToken(String token);

    Optional<User> banUser(long id);

    void deleteUser(long id);
}
