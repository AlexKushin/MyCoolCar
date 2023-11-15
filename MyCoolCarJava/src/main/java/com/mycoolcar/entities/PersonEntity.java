package com.mycoolcar.entities;

import jakarta.persistence.*;
//import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "person", uniqueConstraints = @UniqueConstraint(columnNames = {"email", "contacts"}))
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    boolean inactive;

    boolean ban;

    LocalDateTime registered = LocalDateTime.now();

    private String email;

    @Column
    @NotBlank
    private String name;

    @NotBlank
    private String contacts;

    private String password;

    private String profilePictureUrl;

    //List<CarEntity> personsCars;
}
