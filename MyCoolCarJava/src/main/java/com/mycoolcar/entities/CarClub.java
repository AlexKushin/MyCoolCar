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
import java.util.*;

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

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "car_club_waitlist",
            joinColumns = @JoinColumn(name = "car_club_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    private List<User> waitList = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "userClubs")
    private Set<User> members = new HashSet<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "club_owner_id")
    private User clubOwner;

    @OneToMany(targetEntity = ClubPost.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "carClub")
    private List<ClubPost> clubPosts;

    public void addToWaitlist(User user) {
        if(!this.waitList.contains(user)) {
            this.waitList.add(user);
        }
    }

    public void removeFromWaitlist(User user) {
        this.waitList.remove(user);
    }

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
