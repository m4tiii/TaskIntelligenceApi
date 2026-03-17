package pl.mati.taskintelligenceapi.dto;

import jakarta.validation.constraints.NotBlank;

public record TaskRequestDTO(
        @NotBlank(message = "Title is required")
        String title,
        String description
) {
}
