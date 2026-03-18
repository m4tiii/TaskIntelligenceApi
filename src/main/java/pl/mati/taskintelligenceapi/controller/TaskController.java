package pl.mati.taskintelligenceapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.mati.taskintelligenceapi.dto.TaskRequestDTO;
import pl.mati.taskintelligenceapi.dto.TaskResponseDTO;
import pl.mati.taskintelligenceapi.service.TaskService;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{requestedId}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long requestedId, Principal principal) {
        return ResponseEntity.ok(taskService.getTaskById(requestedId, principal.getName()));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks(Principal principal){
        return ResponseEntity.ok(taskService.getAllTasksByUserUsername(principal.getName()));
    }

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

    @PutMapping("/{requestedId}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long requestedId,
                                                      @Valid @RequestBody TaskRequestDTO taskRequestDTO,
                                                      Principal principal
    ){
        return ResponseEntity.ok(taskService.updateTask(requestedId, taskRequestDTO, principal.getName()));
    }

    @DeleteMapping("/{requestedId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long requestedId, Principal principal){
        taskService.deleteTask(requestedId, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
