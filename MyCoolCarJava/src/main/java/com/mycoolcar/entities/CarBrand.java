package com.mycoolcar.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "car_brands")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CarBrand implements Serializable {

    @Serial
    private static final long serialVersionUID= -7352398296467792867L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    //todo: https://www.baeldung.com/jpa-cascade-types
    @OneToMany(targetEntity = CarModel.class, cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "carBrand")
    private Set<CarModel> carModels = new HashSet<>();

    public CarBrand(String name) {
        this.name = name;
    }

    public CarBrand(String name, Set<CarModel> carModels) {
        this.name = name;
        this.carModels = carModels;
    }
}
