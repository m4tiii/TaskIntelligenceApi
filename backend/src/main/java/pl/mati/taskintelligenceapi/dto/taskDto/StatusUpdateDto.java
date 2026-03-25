package pl.mati.taskintelligenceapi.dto.taskDto;

import jakarta.validation.constraints.NotBlank;
import pl.mati.taskintelligenceapi.entity.enums.TaskStatus;

public record StatusUpdateDto(
        @NotBlank TaskStatus status
) {
}
