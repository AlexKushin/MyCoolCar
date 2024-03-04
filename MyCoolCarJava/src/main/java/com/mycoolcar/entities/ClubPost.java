package com.mycoolcar.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}

