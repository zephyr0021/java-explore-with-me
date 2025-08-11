package ru.practicum.ewm.hit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.ewm.hit.dto.NewEndpointHitRequest;
import ru.practicum.ewm.hit.model.Hit;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface HitMapper {
    @Mapping(source = "timestamp", target = "timestamp", qualifiedByName = "localDateTimeToInstant")
    Hit toHit(NewEndpointHitRequest request);

    @Named("localDateTimeToInstant")
    static Instant localDateTimeToInstant(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return localDateTime.toInstant(ZoneOffset.UTC);
    }
}
