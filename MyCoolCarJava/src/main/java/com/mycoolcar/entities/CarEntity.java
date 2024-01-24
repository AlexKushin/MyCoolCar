package com.mycoolcar.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "cars")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class CarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;

    private String model;

    private int productYear;

    private String description;

    private String image;

    private int rate;

    @ManyToOne(targetEntity=User.class, fetch = FetchType.LAZY)
    @JsonIgnore
    User user;

    public CarEntity(String brand, String model, int productYear, String description, String image) {
        this.brand = brand;
        this.model = model;
        this.productYear = productYear;
        this.description = description;
        this.image = image;

    }

}
