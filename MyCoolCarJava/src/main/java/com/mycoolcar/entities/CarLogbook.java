package com.mycoolcar.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Table(name = "car_logbooks")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class CarLogbook implements Serializable {

    private static final long serialVersionUID = -2914519862840904910L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(targetEntity=CarLogPost.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "carLogbook")
    private List<CarLogPost> carLogPosts;


    @OneToOne(targetEntity= Car.class,cascade=CascadeType.ALL)
    @JsonIgnore
    private Car car;

    public void addCarLog(CarLogPost carLog){
        carLogPosts.add(carLog);
        carLog.setCarLogbook(this);
    }

    public void removeCar(CarLogPost carLog){
        carLogPosts.remove(car);
        carLog.setCarLogbook(null);
    }
}
