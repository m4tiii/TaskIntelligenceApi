package pl.mati.taskintelligenceapi.dto;

import java.time.LocalDateTime;

public record TaskResponseDTO(
        Long id,
        String title,
        String description,
        boolean completed,
        LocalDateTime createdAt
) {
}
