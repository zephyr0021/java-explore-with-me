package ru.practicum.ewm.hit.dto;

import lombok.Data;

@Data
public class HitDto {
    private String app;
    private String uri;
    private Long hits;
}
