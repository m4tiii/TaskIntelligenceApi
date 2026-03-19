package pl.mati.taskintelligenceapi.dto.taskDto;

import pl.mati.taskintelligenceapi.entity.TaskStatus;

import java.time.LocalDateTime;

public record TaskResponseDTO(
        Long id,
        String title,
        String description,
        LocalDateTime createdAt,
        LocalDateTime deadline,
        int importance,
        TaskStatus status,
        double taskPriority
) {
}
