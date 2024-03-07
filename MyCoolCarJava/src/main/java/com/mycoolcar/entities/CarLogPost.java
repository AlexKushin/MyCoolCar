package com.mycoolcar.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Table(name = "car_logs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class CarLogPost extends Post{




    @ManyToOne (targetEntity=CarLogbook.class, fetch = FetchType.LAZY)
    @JsonIgnore
    private CarLogbook carLogbook;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarLogPost that)) return false;
        return Objects.equals(carLogbook, that.carLogbook);
    }

    @Override
    public int hashCode() {
        return Objects.hash(carLogbook);
    }
}
