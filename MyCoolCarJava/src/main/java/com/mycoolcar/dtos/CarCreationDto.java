package com.mycoolcar.dtos;



public record CarCreationDto(String brand, String model,
                             int productYear, String description,
                             String mainImageUrl, String [] imagesUrl, int rate ) {
}
