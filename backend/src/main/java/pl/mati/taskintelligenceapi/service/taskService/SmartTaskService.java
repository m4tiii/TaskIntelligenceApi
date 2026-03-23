package pl.mati.taskintelligenceapi.service.taskService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.mati.taskintelligenceapi.dto.taskDto.TaskResponseDTO;
import pl.mati.taskintelligenceapi.entity.Task;
import pl.mati.taskintelligenceapi.mapper.TaskMapper;
import pl.mati.taskintelligenceapi.repository.TaskRepository;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SmartTaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskPriorityService taskPriorityService;

    public Page<TaskResponseDTO> getSmartTaskList(String username, Pageable pageable){

        Page<Task> taskPage = taskRepository.findAllByUserUsername(pageable, username);



        return taskPage.map(task -> {
            double taskPriorityCalc = taskPriorityService.calculatePriority(task);
            TaskResponseDTO taskResponseDTO = taskMapper.toDto(task);

            return new TaskResponseDTO(
                    taskResponseDTO.id(),
                    taskResponseDTO.title(),
                    taskResponseDTO.description(),
                    taskResponseDTO.createdAt(),
                    taskResponseDTO.deadline(),
                    taskResponseDTO.importance(),
                    taskResponseDTO.status(),
                    taskPriorityCalc
            );
        });
    }


    public List<TaskResponseDTO> getSuggestions(Principal principal) {

        return taskRepository.findAllByUserUsernameAndPriorityScoreGreaterThanSorted(principal.getName(), 40)
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }
}
