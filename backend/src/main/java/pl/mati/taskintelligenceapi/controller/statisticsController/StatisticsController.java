package pl.mati.taskintelligenceapi.controller.statisticsController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mati.taskintelligenceapi.dto.RestResponse;
import pl.mati.taskintelligenceapi.dto.statisticsDto.StatisticsResponseDTO;
import pl.mati.taskintelligenceapi.dto.statisticsDto.StatsRequestDTO;
import pl.mati.taskintelligenceapi.service.statisticsService.StatisticsService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tasks/stats")
@RequiredArgsConstructor
@Tag(name = "Statistics", description = "Endpoints for retrieving user statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Operation(summary = "Get statistics", description = "Retrieves statistics for the authenticated user.")
    @PostMapping
    public ResponseEntity<RestResponse<List<StatisticsResponseDTO>>> getStats(@RequestBody @Valid StatsRequestDTO statsRequestDTO, Principal principal) {
        List<StatisticsResponseDTO> statisticsResponseDTOList = statisticsService.getStats(statsRequestDTO, principal.getName());
        return ResponseEntity.ok(RestResponse.success(statisticsResponseDTOList));
    }
}
