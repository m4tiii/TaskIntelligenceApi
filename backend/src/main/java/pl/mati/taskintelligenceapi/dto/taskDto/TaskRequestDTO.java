package pl.mati.taskintelligenceapi.dto.taskDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import pl.mati.taskintelligenceapi.entity.enums.TaskStatus;

import java.time.LocalDateTime;

public record TaskRequestDTO(
        @NotBlank(message = "Title is required")
        @Size(max = 100, message = "Title cannot be longer than 100 characters")
        String title,
        @Size(max = 255, message = "Description cannot be longer than 255 characters")
        String description,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime deadline,
        @Max(10) @Min(1)
        int importance,
        TaskStatus status
) {
}
