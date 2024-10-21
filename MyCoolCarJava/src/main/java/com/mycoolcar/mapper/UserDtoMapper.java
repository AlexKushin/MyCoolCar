package com.mycoolcar.mapper;

import com.mycoolcar.dtos.UserDto;
import com.mycoolcar.entities.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDtoMapper implements Function<User, UserDto> {
    @Override
    public UserDto apply(User user) {
        return new UserDto(
                user.getId(),
                user.isBan(),
                user.getRegistered(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.isEnabled()
        );
    }
}
