package pl.mati.taskintelligenceapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.mati.taskintelligenceapi.entity.Task;
import pl.mati.taskintelligenceapi.entity.TaskStatus;
import pl.mati.taskintelligenceapi.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskSchedulderService {
    private final TaskRepository taskRepository;
    private final TaskPriorityService taskPriorityService;

    @Scheduled(fixedRate = 3600000)
    public void updateAllPriorities(){
        List<Task> tasks = taskRepository.findAllByTaskStatusNot(TaskStatus.COMPLETED);

        tasks.forEach(task -> {
            double newScore = taskPriorityService.calculatePriority(task);
            task.setPriorityScore(newScore);
        });

        taskRepository.saveAll(tasks);
        System.out.println("Priorities updated! Done at: " + LocalDateTime.now());
    }
}
