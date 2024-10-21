package com.mycoolcar.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mycoolcar.enums.CarClubAccessType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Table(name = "car_clubs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class CarClub implements Serializable {

    private static final long serialVersionUID = 4150751772073386615L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String location;

    private LocalDateTime createdTime;

    private CarClubAccessType accessType;

    //Many to Many Bidirectional
    @JsonIgnore
    @ManyToMany(mappedBy = "userClubs")
    private Set<User> members = new HashSet<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "club_owner_id")
    private User clubOwner;

    @OneToMany(targetEntity = ClubPost.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "carClub")
    private List<ClubPost> clubPosts;

    public void addCarClubMember(User user) {
        this.members.add(user);
        user.getUserClubs().add(this);
    }

    public void removeCarClubMember(User user){
        this.members.remove(user);
        user.getUserClubs().remove(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarClub carClub)) return false;
        return Objects.equals(id, carClub.id) && Objects.equals(description, carClub.description)
                && Objects.equals(clubPosts, carClub.clubPosts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, clubPosts);
    }
}
