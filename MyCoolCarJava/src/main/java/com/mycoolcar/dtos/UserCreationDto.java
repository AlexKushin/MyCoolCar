package com.mycoolcar.dtos;

import com.mycoolcar.entities.Role;

import java.util.Set;

public record UserCreationDto(
        String firstName,
        String lastName,
        String password,
        String matchingPassword,
        String email,
        Set<Role> roles) {

}
