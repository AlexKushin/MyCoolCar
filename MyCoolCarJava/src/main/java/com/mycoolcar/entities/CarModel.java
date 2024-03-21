package com.mycoolcar.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="car_models")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CarModel {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private  int id;

    private  String name;
    @ManyToOne(targetEntity=CarBrand.class, fetch = FetchType.LAZY)
    @JsonIgnore
    private CarBrand carBrand;

    public CarModel(String name) {
        this.name = name;
    }
}
