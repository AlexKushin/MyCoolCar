package com.mycoolcar.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Table(name = "car_logbooks")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class CarLogbook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(targetEntity=CarLogPost.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "carLogbook")
    private List<CarLogPost> carLogPosts;


    @OneToOne(targetEntity=CarEntity.class,cascade=CascadeType.ALL)
    @JsonIgnore
    private CarEntity car;

    public void addCarLog(CarLogPost carLog){
        carLogPosts.add(carLog);
        carLog.setCarLogbook(this);
    }

    public void removeCar(CarLogPost carLog){
        carLogPosts.remove(car);
        carLog.setCarLogbook(null);
    }
}
