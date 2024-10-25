package com.mycoolcar.mapper;

import com.mycoolcar.dtos.CarClubDto;
import com.mycoolcar.entities.CarClub;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CarClubDtoMapper implements Function<CarClub, CarClubDto> {
    @Override
    public CarClubDto apply(CarClub carClub) {
        return new CarClubDto(carClub.getId(),
                carClub.getName(),
                carClub.getDescription(),
                carClub.getLocation(),
                carClub.getCreatedTime(),
                carClub.getAccessType(),
                carClub.getWaitList(),
                carClub.getMembers(),
                carClub.getClubPosts(),
                carClub.getClubOwner().getId());
    }
}
