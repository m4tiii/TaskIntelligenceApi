package pl.mati.taskintelligenceapi.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mati.taskintelligenceapi.dto.TaskRequestDTO;
import pl.mati.taskintelligenceapi.dto.TaskResponseDTO;
import pl.mati.taskintelligenceapi.entity.Task;
import pl.mati.taskintelligenceapi.entity.User;
import pl.mati.taskintelligenceapi.repository.TaskRepository;
import pl.mati.taskintelligenceapi.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public List<TaskResponseDTO> getAllTasksByUserUsername(String username){
        List<Task> tasks = taskRepository.findAllByUserUsername(username);
        return tasks.stream()
                .map(this::mapToDto)
                .toList();
    }

    public TaskResponseDTO getTaskById(Long id, String username){
        Task task = taskRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new EntityNotFoundException("Task with id: " + id + ", that belongs to user: " + username + " not found!"));
        return mapToDto(task);
    }

    @Transactional
    public TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO, String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + username + " not found!"));
        Task task = mapToTask(taskRequestDTO);
        task.setUser(user);
        return mapToDto(taskRepository.save(task));
    }

    @Transactional
    public TaskResponseDTO updateTask(Long id, TaskRequestDTO taskRequestDTO, String username){
        Task task = taskRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Task with id: " + id + ", that belongs to user: " + username + " not found!"
                ));
        task.setTitle(taskRequestDTO.title());
        task.setDescription(taskRequestDTO.description());
        return mapToDto(task);
    }

    @Transactional
    public void deleteTask(Long id, String username){
        Task taskToDelete = taskRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Task with id: " + id + ", that belongs to user: " + username + " not found!"
                ));
        taskRepository.delete(taskToDelete);
    }

    private TaskResponseDTO mapToDto(Task task){
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCompleted() != null ? task.getCompleted() : false, // Zabezpieczenie przed starymi zadaniami w bazie
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
