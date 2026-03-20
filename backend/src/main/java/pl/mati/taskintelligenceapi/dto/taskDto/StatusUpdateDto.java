package pl.mati.taskintelligenceapi.dto.taskDto;

import jakarta.validation.constraints.NotBlank;

public record StatusUpdateDto(
        @NotBlank String status
) {
}
