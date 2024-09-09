package com.mycoolcar.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
public class Post implements Serializable {

    private static final long serialVersionUID = -6747914979222144881L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topic;

    private String description;

    private LocalDateTime createdTime;

    private boolean isEdited;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post post)) return false;
        return Objects.equals(id, post.id) && Objects.equals(topic, post.topic)
                && Objects.equals(description, post.description)
                && Objects.equals(createdTime, post.createdTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, topic, description, createdTime);
    }
}
