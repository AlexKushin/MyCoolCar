package com.mycoolcar.dtos;

public record UserCreationDto(
        String firstName,
        String lastName,
        String password,
        String email){

}
