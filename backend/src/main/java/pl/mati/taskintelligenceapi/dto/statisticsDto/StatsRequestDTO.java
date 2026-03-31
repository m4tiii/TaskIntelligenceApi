package pl.mati.taskintelligenceapi.dto.statisticsDto;

import jakarta.validation.constraints.NotNull;
import pl.mati.taskintelligenceapi.entity.enums.TimeRange;

import java.time.OffsetDateTime;

public record StatsRequestDTO(
        @NotNull OffsetDateTime from,
        @NotNull OffsetDateTime to,
        @NotNull TimeRange timeRange
) {
}
