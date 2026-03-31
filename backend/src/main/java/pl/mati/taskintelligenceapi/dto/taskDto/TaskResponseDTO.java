package pl.mati.taskintelligenceapi.dto.taskDto;

import pl.mati.taskintelligenceapi.entity.enums.TaskStatus;

import java.time.OffsetDateTime;

public record TaskResponseDTO(
        Long id,
        String title,
        String description,
        OffsetDateTime createdAt,
        OffsetDateTime startAt,
        OffsetDateTime deadline,
        int importance,
        TaskStatus taskStatus,
        double priorityScore
) {
}
