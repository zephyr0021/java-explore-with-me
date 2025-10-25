package ru.practicum.ewm.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CompilationRequest {
    private List<Long> events;

    private Boolean pinned = Boolean.FALSE;

    @NotBlank
    @Size(min = 1, max = 50)
    private String title;


}
