package com.mycoolcar.services;

import com.mycoolcar.dtos.UserCreationDto;
import com.mycoolcar.dtos.UserDto;
import com.mycoolcar.entities.User;
import com.mycoolcar.entities.VerificationToken;
import com.mycoolcar.exceptions.UserAlreadyExistException;
import com.mycoolcar.util.ApiResponse;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.context.request.WebRequest;


public interface IUserService extends UserDetailsService {
    UserDto registerNewUserAccount(UserCreationDto userDto, WebRequest request)
            throws UserAlreadyExistException;

    User getUserByVerificationToken(String verificationToken);

    void saveRegisteredUser(User user);

    void createVerificationTokenForUser(User user, String token);

    VerificationToken getVerificationToken(String verificationToken);

    void deleteVerificationToken(String token);

    String validatePasswordResetToken(String token);

    UserDto banUser(long id);

    ApiResponse deleteUser(long id, WebRequest request);
}
