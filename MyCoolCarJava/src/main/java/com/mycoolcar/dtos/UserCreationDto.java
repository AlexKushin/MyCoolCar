package com.mycoolcar.dtos;

import com.mycoolcar.validation.PasswordMatches;
import com.mycoolcar.validation.ValidEmail;
import com.mycoolcar.validation.ValidPassword;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@PasswordMatches
public record UserCreationDto(
        @NotNull
        @NotEmpty
        String firstName,
        @NotNull
        @NotEmpty
        String lastName,
        @NotNull
        @NotEmpty
        @ValidPassword
        String password,
        String matchingPassword,
        @NotNull
        @NotEmpty
        @ValidEmail
        String email){

}
