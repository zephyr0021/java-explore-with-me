package ru.practicum.ewm.hit.mapper;

import org.mapstruct.Mapper;

import ru.practicum.ewm.hit.dto.HitDto;
import ru.practicum.ewm.hit.dto.NewEndpointHitRequest;
import ru.practicum.ewm.hit.model.Hit;
import ru.practicum.ewm.hit.repository.HitShort;

@Mapper(componentModel = "spring")
public interface HitMapper {
    Hit toHit(NewEndpointHitRequest request);

    HitDto toHitDto(HitShort hitShort);
}
