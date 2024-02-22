package com.mycoolcar.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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


}
