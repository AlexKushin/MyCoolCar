package com.mycoolcar.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CarEditDto {
    private String brand;
    private String model;
    private int productYear;
    private String description;

}
