package com.mycoolcar.dtos;

import com.mycoolcar.entities.Role;

import java.util.Set;

public record UserDto(Long id, boolean ban, String firstName, String lastName, String email, boolean enabled,
                      Set<Role> roles) {
}
