package com.mycoolcar.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Table(name = "cars")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Car implements Serializable {

    private static final long serialVersionUID = -9147379439939477588L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;

    private String model;

    private int productYear;

    private String description;

    private String mainImageUrl;

    private String[] imagesUrl;

    private int rate;

    @OneToOne(targetEntity = CarLogbook.class, cascade = CascadeType.ALL)
    @Null
    private CarLogbook logbook;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JsonIgnore
    User user;

    public Car(String brand, String model, int productYear, String description) {
        this.brand = brand;
        this.model = model;
        this.productYear = productYear;
        this.description = description;

    }

}
