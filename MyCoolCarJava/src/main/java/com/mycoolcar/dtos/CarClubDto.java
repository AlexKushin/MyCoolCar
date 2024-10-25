package com.mycoolcar.dtos;


import com.mycoolcar.entities.ClubPost;
import com.mycoolcar.entities.User;
import com.mycoolcar.enums.CarClubAccessType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record CarClubDto(Long id,
                         String name,
                         String description,
                         String location,
                         LocalDateTime createdTime,
                         CarClubAccessType accessType,
                         List<User> waitList,
                         Set<User> members,
                         List<ClubPost> posts,
                         //User clubOwner?
                         long clubOwnerId) {
}
