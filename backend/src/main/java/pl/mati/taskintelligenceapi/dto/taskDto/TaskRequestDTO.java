package pl.mati.taskintelligenceapi.dto.taskDto;

import jakarta.validation.constraints.*;
import pl.mati.taskintelligenceapi.entity.enums.TaskStatus;

import java.time.OffsetDateTime;

public record TaskRequestDTO(
        @NotBlank(message = "Title is required")
        @Size(max = 100, message = "Title cannot be longer than 100 characters")
        String title,
        @Size(max = 255, message = "Description cannot be longer than 255 characters")
        String description,
        @Future
        @NotNull
        OffsetDateTime startAt,
        @Future
        @NotNull
        OffsetDateTime deadline,
        @Max(10) @Min(1)
        @NotNull
        Integer importance,
        TaskStatus taskStatus
) {
}
