package pl.mati.taskintelligenceapi.controller.taskController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.mati.taskintelligenceapi.dto.taskDto.TaskRequestDTO;
import pl.mati.taskintelligenceapi.dto.taskDto.TaskResponseDTO;
import pl.mati.taskintelligenceapi.service.taskService.TaskService;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Endpoints for managing user tasks and priorities")
public class TaskController {

    private final TaskService taskService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "403", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @Operation(summary = "Get task by ID", description = "Retrieves a specific task by its ID and authenticated user.")
    @GetMapping("/{requestedId}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long requestedId, Principal principal) {
        return ResponseEntity.ok(taskService.getTaskById(requestedId, principal.getName()));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "403", description = "Unauthorized - invalid or missing JWT")
    })
    @Operation(summary = "Get all tasks", description = "Retrieves all tasks for the authenticated user.")
    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks(Principal principal){
        return ResponseEntity.ok(taskService.getAllTasksByUserUsername(principal.getName()));
    }

    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "403", description = "Unauthorized - invalid or missing JWT")
    })
    @Operation(summary = "Get page of tasks", description = "Retrieves a page of tasks for the authenticated user.")
    @GetMapping("/page")
    public ResponseEntity<Page<TaskResponseDTO>> getPageOfTasks(Pageable pageable, Principal principal){
        return ResponseEntity.ok(taskService.getPageOfTasks(pageable, principal.getName()));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created"),
            @ApiResponse(responseCode = "403", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @Operation(summary = "Save task", description = "Saves a new task for the authenticated user.")
    @PostMapping
    public ResponseEntity<TaskResponseDTO> saveTask(@Valid @RequestBody TaskRequestDTO taskRequestDTO,
                                                    Principal principal
    ) {
        TaskResponseDTO savedTask = taskService.createTask(taskRequestDTO, principal.getName());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedTask.id())
                .toUri();

        return ResponseEntity.created(location).body(savedTask);
    }



    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "403", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @Operation(summary = "Update task", description = "Updates an existing task for the authenticated user.")
    @PutMapping("/{requestedId}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long requestedId,
                                                      @Valid @RequestBody TaskRequestDTO taskRequestDTO,
                                                      Principal principal
    ){
        return ResponseEntity.ok(taskService.updateTask(requestedId, taskRequestDTO, principal.getName()));
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted"),
            @ApiResponse(responseCode = "403", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @Operation(summary = "Delete task", description = "Deletes an existing task for the authenticated user.")
    @DeleteMapping("/{requestedId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long requestedId, Principal principal){
        taskService.deleteTask(requestedId, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
