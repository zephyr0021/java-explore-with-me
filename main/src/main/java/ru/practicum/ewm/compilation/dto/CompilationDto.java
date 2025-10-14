package ru.practicum.ewm.compilation.dto;

import lombok.Data;
import ru.practicum.ewm.event.dto.EventDto;

import java.util.List;

@Data
public class CompilationDto {
    private List<EventDto> events;

    private Long id;

    private Boolean pinned;

    private String title;


}
