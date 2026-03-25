package pl.mati.taskintelligenceapi.controller.taskController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mati.taskintelligenceapi.dto.RestResponse;
import pl.mati.taskintelligenceapi.dto.taskDto.TaskResponseDTO;
import pl.mati.taskintelligenceapi.service.taskService.SmartTaskService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tasks/smart")
@RequiredArgsConstructor
@Tag(name = "Smart Tasks", description = "Endpoints for managing smart tasks")
public class SmartTaskController {

    private final SmartTaskService smartTaskService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "403", description = "Unauthorized - invalid or missing JWT")
    })
    @Operation(summary = "Get all smart tasks", description = "Retrieves all smart tasks for the authenticated user.")
    @GetMapping("/getAllSmartTasks")
    public ResponseEntity<RestResponse<Page<TaskResponseDTO>>> getAllSmartTasks(Principal principal, Pageable pageable){
        Page<TaskResponseDTO> taskPage = smartTaskService.getSmartTaskList(principal.getName(), pageable);
        return ResponseEntity.ok(RestResponse.success(taskPage));
    }

    @GetMapping("/suggestions")
    public ResponseEntity<RestResponse<List<TaskResponseDTO>>> getSuggestions(Principal principal){
        List<TaskResponseDTO> suggestions = smartTaskService.getSuggestions(principal);
        return ResponseEntity.ok(RestResponse.success(suggestions));
    }
}
