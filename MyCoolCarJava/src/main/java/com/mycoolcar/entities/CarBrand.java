package com.mycoolcar.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name="car_brands")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CarBrand {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private  int id;

    private  String name;

    @OneToMany(targetEntity = CarModel.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "carBrand")
    //private Set<CarModel> carModels;
    private List<CarModel> carModels;

    public CarBrand(String name) {
        this.name = name;
    }

    public CarBrand(String name, List<CarModel> carModels) {
        this.name = name;
        this.carModels = carModels;
    }
}
