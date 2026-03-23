package pl.mati.taskintelligenceapi.service.taskService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.mati.taskintelligenceapi.dto.taskDto.TaskResponseDTO;
import pl.mati.taskintelligenceapi.entity.Task;
import pl.mati.taskintelligenceapi.entity.TaskStatus;
import pl.mati.taskintelligenceapi.mapper.TaskMapper;
import pl.mati.taskintelligenceapi.repository.TaskRepository;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SmartTaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskPriorityService taskPriorityService;

    public Page<TaskResponseDTO> getSmartTaskList(String username, Pageable pageable){
        List<TaskResponseDTO> tasks = taskRepository.findAllByUserUsername(username).stream()
                .filter(t -> t.getTaskStatus() != TaskStatus.COMPLETED)
                .peek(t -> t.setPriorityScore(taskPriorityService.calculatePriority(t)))
                .sorted(Comparator.comparingDouble(Task::getPriorityScore).reversed())
                .map(taskMapper::toDto)
                .toList();

        return getPageFromFullList(tasks, pageable);
    }

    public Page<TaskResponseDTO> getPageFromFullList(List<TaskResponseDTO> allTasks, Pageable pageable){
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allTasks.size());

        if(start > allTasks.size()){
            return new PageImpl<>(List.of(), pageable, allTasks.size());
        }

        List<TaskResponseDTO> subList = allTasks.subList(start, end);
        return new PageImpl<>(subList, pageable, allTasks.size());
    }

    public List<TaskResponseDTO> getSuggestions(Principal principal) {

        return taskRepository.findAllByUserUsernameAndPriorityScoreGreaterThanSorted(principal.getName(), 30)
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }
}
