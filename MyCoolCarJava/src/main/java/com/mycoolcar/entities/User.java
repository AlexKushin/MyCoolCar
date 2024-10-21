package com.mycoolcar.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mycoolcar.enums.AppUserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class User implements UserDetails, Serializable {

    private static final long serialVersionUID = -2338113688315793511L;
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean ban;

    private LocalDateTime registered = LocalDateTime.now();

    private String firstName;

    private String lastName;

    private String email;

    @JsonIgnore
    @Column(length = 60)
    private String password;

    private boolean enabled;

    private Set<AppUserRole> roles = new HashSet<>();

    @JsonIgnore
    @OneToMany(targetEntity = Car.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private List<Car> userCars = new ArrayList<>();

    //Many to Many Bidirectional
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_clubs",
            joinColumns =
            @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns =
            @JoinColumn(name = "car_club_id", referencedColumnName = "id"))
    private Set<CarClub> userClubs = new HashSet<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_subscribed_cars",
            joinColumns =
            @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns =
            @JoinColumn(name = "car_id", referencedColumnName = "id"))
    private List<Car> subscribedCars = new ArrayList<>();

    public User(String firstName, String lastName,
                String email, String password, Set<AppUserRole> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
        if (roles != null) {
            authorities = roles.stream().map(p -> new SimpleGrantedAuthority("ROLE_" + p.name()))
                    .collect(Collectors.toUnmodifiableSet());
        }
        return authorities;
    }


    @Override
    public String getUsername() {
        return email;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    @Override
    public boolean isAccountNonLocked() {
        return true;
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return ban == user.ban && enabled == user.enabled && Objects.equals(id, user.id)
                && Objects.equals(registered, user.registered) && Objects.equals(firstName, user.firstName)
                && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email)
                && Objects.equals(password, user.password) && Objects.equals(roles, user.roles)
                && Objects.equals(userCars, user.userCars) && Objects.equals(userClubs, user.userClubs)
                && Objects.equals(subscribedCars, user.subscribedCars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ban, registered, firstName, lastName, email, password,
                enabled, roles, userCars, userClubs, subscribedCars);
    }
}
