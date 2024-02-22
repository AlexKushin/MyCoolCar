package com.mycoolcar.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "car_clubs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class CarClub  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    @OneToMany(targetEntity = ClubPost.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "carClub")
    private List<ClubPost> clubPosts;
}
