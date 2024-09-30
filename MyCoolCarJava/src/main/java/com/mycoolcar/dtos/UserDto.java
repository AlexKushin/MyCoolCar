package com.mycoolcar.dtos;

import java.time.LocalDateTime;


public record UserDto(Long id, boolean ban, LocalDateTime registered,
                      String firstName, String lastName,
                      String email, boolean enabled) {
}
