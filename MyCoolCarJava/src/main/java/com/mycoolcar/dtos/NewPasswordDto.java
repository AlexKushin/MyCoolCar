package com.mycoolcar.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


public record NewPasswordDto(String token, @NotNull @NotEmpty String password, String matchingPassword) {
}
