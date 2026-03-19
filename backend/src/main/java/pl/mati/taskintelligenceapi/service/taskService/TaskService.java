package pl.mati.taskintelligenceapi.service.taskService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mati.taskintelligenceapi.dto.taskDto.TaskRequestDTO;
import pl.mati.taskintelligenceapi.dto.taskDto.TaskResponseDTO;
import pl.mati.taskintelligenceapi.entity.Task;
import pl.mati.taskintelligenceapi.entity.TaskStatus;
import pl.mati.taskintelligenceapi.entity.User;
import pl.mati.taskintelligenceapi.mapper.TaskMapper;
import pl.mati.taskintelligenceapi.repository.TaskRepository;
import pl.mati.taskintelligenceapi.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskPriorityService taskPriorityService;
    private final TaskMapper taskMapper;

    public List<TaskResponseDTO> getAllTasksByUserUsername(String username){
        List<Task> tasks = taskRepository.findAllByUserUsername(username);
        return tasks.stream()
                .map(taskMapper::mapToDto)
                .toList();
    }


    public TaskResponseDTO getTaskById(Long id, String username){
        Task task = taskRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new EntityNotFoundException("Task with id: " + id + ", that belongs to user: " + username + " not found!"));
        return taskMapper.mapToDto(task);
    }

    @Transactional
    public TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO, String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + username + " not found!"));
        Task task = taskMapper.mapToTask(taskRequestDTO);
        task.setUser(user);
        task.setTaskStatus(TaskStatus.NEW);
        return taskMapper.mapToDto(taskRepository.save(task));
    }

    @Transactional
    public TaskResponseDTO updateTask(Long id, TaskRequestDTO taskRequestDTO, String username){
        Task task = taskRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Task with id: " + id + ", that belongs to user: " + username + " not found!"
                ));
        task.setTitle(taskRequestDTO.title());
        task.setDescription(taskRequestDTO.description());
        task.setTaskStatus(taskRequestDTO.status());
        task.setImportance(taskRequestDTO.importance());
        task.setDeadlineTo(taskRequestDTO.deadline());
        task.setPriorityScore(taskPriorityService.calculatePriority(task));
        return taskMapper.mapToDto(task);
    }

    @Transactional
    public void deleteTask(Long id, String username){
        Task taskToDelete = taskRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Task with id: " + id + ", that belongs to user: " + username + " not found!"
                ));
        taskRepository.delete(taskToDelete);
    }

    public Page<TaskResponseDTO> getPageOfTasks(Pageable pageable, String name) {
        Page<Task> tasks = taskRepository.findAllByUserUsername(pageable, name);

        return tasks.map(taskMapper::mapToDto);
    }
}
