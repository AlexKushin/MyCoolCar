package com.mycoolcar.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Table(name = "car_club_posts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ClubPost extends Post{

    @ManyToOne(targetEntity=CarClub.class, fetch = FetchType.LAZY)
    @JsonIgnore
    private CarClub carClub;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClubPost clubPost)) return false;
        return Objects.equals(carClub, clubPost.carClub);
    }

    @Override
    public int hashCode() {
        return Objects.hash(carClub);
    }
}

