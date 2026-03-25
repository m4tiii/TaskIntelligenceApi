package pl.mati.taskintelligenceapi.service.taskService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.mati.taskintelligenceapi.entity.Task;
import pl.mati.taskintelligenceapi.entity.enums.TaskStatus;
import pl.mati.taskintelligenceapi.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskSchedulerService {
    private final TaskRepository taskRepository;
    private final TaskPriorityService taskPriorityService;

    @Scheduled(fixedRate = 3600000)
    public void updateAllPriorities(){
        List<Task> tasks = taskRepository.findAllByTaskStatusNot(TaskStatus.COMPLETED);

        tasks.forEach(task -> {
            double newScore = taskPriorityService.calculatePriorityScore(task);
            task.setPriorityScore(newScore);
        });

        taskRepository.saveAll(tasks);
        log.info("Priorities updated! Done at: {}", LocalDateTime.now());
    }
}
