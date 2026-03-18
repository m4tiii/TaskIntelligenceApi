package pl.mati.taskintelligenceapi.dto;

import pl.mati.taskintelligenceapi.entity.TaskStatus;

import java.time.LocalDate;

public record TaskResponseDTO(
        Long id,
        String title,
        String description,
        LocalDate createdAt,
        LocalDate deadline,
        int importance,
        TaskStatus status,
        double taskPriority
) {
}
