package pl.mati.taskintelligenceapi.dto.statisticsDto;

import jakarta.validation.constraints.NotNull;
import pl.mati.taskintelligenceapi.entity.TimeRange;

import java.time.LocalDate;

public record StatsRequestDTO(
        @NotNull LocalDate from,
        @NotNull LocalDate to,
        @NotNull TimeRange timeRange
) {
}
