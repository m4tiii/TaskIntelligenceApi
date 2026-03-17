package pl.mati.taskintelligenceapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskRequestDTO(
        @NotBlank(message = "Title is required")
        @Size(max = 100, message = "Title cannot be longer than 100 characters")
        String title,
        @Size(max = 255, message = "Description cannot be longer than 255 characters")
        String description
) {
}
