package com.mycoolcar.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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

    private List<String> imagesUrl;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car car)) return false;
        return productYear == car.productYear && rate == car.rate && Objects.equals(id, car.id)
                && Objects.equals(brand, car.brand) && Objects.equals(model, car.model)
                && Objects.equals(description, car.description) && Objects.equals(mainImageUrl, car.mainImageUrl)
                && Objects.equals(imagesUrl, car.imagesUrl) && Objects.equals(logbook, car.logbook)
                && Objects.equals(user, car.user);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, brand, model, productYear, description, mainImageUrl, rate, logbook, user);
        result = 31 * result + Objects.hashCode(imagesUrl);
        return result;
    }
}
