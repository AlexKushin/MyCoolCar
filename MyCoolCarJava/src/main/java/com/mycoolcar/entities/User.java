package com.mycoolcar.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
@Entity
@AllArgsConstructor
@Getter
@Setter
public class User implements UserDetails, Serializable {

    private static final long serialVersionUID = -2338113688315793511L;
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    boolean ban;

    LocalDateTime registered = LocalDateTime.now();

    private String firstName;

    private String lastName;

    private String email;

    @Column(length = 60)
    private String password;

    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_roles",
            joinColumns =
            @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns =
            @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(targetEntity = Car.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    List<Car> userCars = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_clubs",
            joinColumns =
            @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns =
            @JoinColumn(name = "car_club_id", referencedColumnName = "id"))
    List<CarClub> userClubs = new ArrayList<>();

   @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
   @JoinTable(name = "user_subscribed_cars",
           joinColumns =
           @JoinColumn(name = "user_id", referencedColumnName = "id"),
           inverseJoinColumns =
           @JoinColumn(name = "car_id", referencedColumnName = "id"))
    List<Car> subscribedCars = new ArrayList<>();


    public User() {
        super();
        this.enabled = false;
    }
    public void addCar(Car car) {
        userCars.add(car);
        car.setUser(this);
    }

    public void removeCar(Car car) {
        userCars.remove(car);
        car.setUser(null);
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
        if (roles != null) {
            authorities = roles.stream().map(p -> new SimpleGrantedAuthority("ROLE_" + p.getRoleName()))
                    .collect(Collectors.toUnmodifiableSet());
        }
        return authorities;
    }


    @JsonIgnore
    @Override
    public String getUsername() {
        return firstName;
    }


    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }


    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
