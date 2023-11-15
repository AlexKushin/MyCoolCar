package com.mycoolcar.entities;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "car")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String brand;

    private String model;

    private int productYear;

    private String description;

    @ManyToOne
    PersonEntity personEntity;

    public CarEntity(String brand, String model, int productYear, String description, PersonEntity personEntity) {
        this.brand = brand;
        this.model = model;
        this.productYear = productYear;
        this.description = description;
        this.personEntity = personEntity;
    }
}
