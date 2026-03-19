package pl.mati.taskintelligenceapi.controller.taskController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mati.taskintelligenceapi.dto.taskDto.TaskResponseDTO;
import pl.mati.taskintelligenceapi.service.taskService.SmartTaskService;

import java.security.Principal;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Smart Tasks", description = "Endpoints for managing smart tasks")
public class SmartTaskController {

    private final SmartTaskService smartTaskService;

    @Operation(summary = "Get all smart tasks", description = "Retrieves all smart tasks for the authenticated user.")
    @GetMapping("/smartTasks")
    public ResponseEntity<Page<TaskResponseDTO>> getAllSmartTasks(Principal principal, Pageable pageable){
        return ResponseEntity.ok(smartTaskService.getSmartTaskList(principal.getName(), pageable));
    }
}
