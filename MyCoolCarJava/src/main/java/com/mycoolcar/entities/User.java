package com.mycoolcar.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class User implements UserDetails {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    boolean ban;

    LocalDateTime registered = LocalDateTime.now();

    private String firstName;

    private String lastName;

    //private String profilePictureUrl;

    private String email;

    @Column(length = 60)
    private String password;

  //  private boolean isUsing2FA;

   // private String secret;

    //todo: read about CascadeTypes https://www.baeldung.com/jpa-cascade-types

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_roles",
            joinColumns =
            @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns =
            @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;


    //List<CarEntity> personsCars;
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
}
