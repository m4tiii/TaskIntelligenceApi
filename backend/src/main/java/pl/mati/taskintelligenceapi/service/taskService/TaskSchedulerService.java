package pl.mati.taskintelligenceapi.service.taskService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.mati.taskintelligenceapi.entity.Task;
import pl.mati.taskintelligenceapi.entity.enums.TaskStatus;
import pl.mati.taskintelligenceapi.repository.TaskRepository;
import pl.mati.taskintelligenceapi.service.notificationService.NotificationDispatcher;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskSchedulerService {
    private final TaskRepository taskRepository;
    private final TaskPriorityService taskPriorityService;
    private final NotificationDispatcher notificationDispatcher;

    @Scheduled(fixedRate = 3600000)
    public void updateAllPriorities(){
        List<Task> tasks = taskRepository.findAllByTaskStatusNot(TaskStatus.COMPLETED);

        tasks.forEach(task -> {
            double newScore = taskPriorityService.calculatePriorityScore(task);
            task.setPriorityScore(newScore);
            if(LocalDateTime.now().isAfter(task.getDeadline())){
                notificationDispatcher.sendOverdueAlert(task.getUser(), task.getId(), task.getTitle());
            }else if(ChronoUnit.HOURS.between(LocalDateTime.now(), task.getDeadline()) <= 1){
                notificationDispatcher.sendCloseToOverdueAlert(task.getUser(), task.getId(), task.getTitle());
            }
        });

        taskRepository.saveAll(tasks);
        log.info("Priorities updated! Done at: {}", LocalDateTime.now());
    }
}
