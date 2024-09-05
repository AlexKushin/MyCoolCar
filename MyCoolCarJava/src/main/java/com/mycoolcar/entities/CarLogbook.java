package com.mycoolcar.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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


    @OneToOne(mappedBy = "logbook")
    @JsonIgnore
    private Car car;

    // Custom getter for car id
    @JsonProperty("carId")
    public Long getCarId() {
        return (car != null) ? car.getId() : null;
    }

    public void addCarLog(CarLogPost carLog){
        carLogPosts.add(carLog);
        carLog.setCarLogbook(this);
    }

    public void removeCarLog(CarLogPost carLog){
        carLogPosts.remove(carLog);
        carLog.setCarLogbook(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarLogbook that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(carLogPosts, that.carLogPosts)
                && Objects.equals(car, that.car);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, carLogPosts);
    }
}
