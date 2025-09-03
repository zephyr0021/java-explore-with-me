package ru.practicum.ewm.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewUserRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String email;
}
