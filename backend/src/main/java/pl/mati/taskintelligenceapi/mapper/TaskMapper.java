package pl.mati.taskintelligenceapi.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.mati.taskintelligenceapi.dto.taskDto.TaskRequestDTO;
import pl.mati.taskintelligenceapi.dto.taskDto.TaskResponseDTO;
import pl.mati.taskintelligenceapi.entity.Task;
import pl.mati.taskintelligenceapi.service.taskService.TaskPriorityService;

@Component
@RequiredArgsConstructor
public class TaskMapper {

    private final TaskPriorityService taskPriorityService;

    public TaskResponseDTO mapToDto(Task task){
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCreatedAt(),
                task.getDeadlineTo(),
                task.getImportance(),
                task.getTaskStatus(),
                task.getPriorityScore()
        );
    }

    public Task mapToTask(TaskRequestDTO taskRequestDTO){
        Task task = new Task();
        task.setTitle(taskRequestDTO.title());
        task.setDescription(taskRequestDTO.description());
        task.setDeadlineTo(taskRequestDTO.deadline());
        task.setImportance(taskRequestDTO.importance());
        task.setPriorityScore(taskPriorityService.calculatePriority(task));
        return task;
    }
}
