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
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{requestedId}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long requestedId){
        return ResponseEntity.ok(taskService.getTaskById(requestedId));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks(){
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> saveTask(@Valid @RequestBody TaskRequestDTO taskRequestDTO){
        TaskResponseDTO savedTask = taskService.createTask(taskRequestDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedTask.id())
                .toUri();

        return ResponseEntity.created(location).body(savedTask);
    }

    @PutMapping("/{requestedId}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long requestedId, @Valid @RequestBody TaskRequestDTO taskRequestDTO){
        return ResponseEntity.ok(taskService.updateTask(requestedId, taskRequestDTO));
    }

    @DeleteMapping("/{requestedId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long requestedId){
        taskService.deleteTask(requestedId);
        return ResponseEntity.noContent().build();
    }
}
