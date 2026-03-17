package pl.mati.taskintelligenceapi.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mati.taskintelligenceapi.dto.TaskRequestDTO;
import pl.mati.taskintelligenceapi.dto.TaskResponseDTO;
import pl.mati.taskintelligenceapi.entity.Task;
import pl.mati.taskintelligenceapi.repository.TaskRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public List<TaskResponseDTO> getAllTasks(){
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(this::mapToDto)
                .toList();
    }

    public TaskResponseDTO getTaskById(Long id){
        Task task = taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task o id: " + id + " nie istnieje!"));
        return mapToDto(task);
    }

    @Transactional
    public TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO){
        Task task = mapToTask(taskRequestDTO);
        return mapToDto(taskRepository.save(task));
    }

    @Transactional
    public TaskResponseDTO updateTask(Long id, TaskRequestDTO taskRequestDTO){
        Task task = taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task o id: " + id + " nie istnieje!"));
        task.setTitle(taskRequestDTO.title());
        task.setDescription(taskRequestDTO.description());
        return mapToDto(task);
    }

    @Transactional
    public void deleteTask(Long id){
        Task taskToDelete = taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task o id: " + id + " nie istnieje!"));
        taskRepository.delete(taskToDelete);
    }

    private TaskResponseDTO mapToDto(Task task){
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCompleted(),
                task.getCreatedAt()
        );
    }

    private Task mapToTask(TaskRequestDTO taskRequestDTO){
        Task task = new Task();
        task.setTitle(taskRequestDTO.title());
        task.setDescription(taskRequestDTO.description());
        task.setCompleted(false);
        return task;
    }
}
