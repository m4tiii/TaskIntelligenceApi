package pl.mati.taskintelligenceapi.service.taskService;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
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
    private final EntityManager entityManager;
    private final TransactionTemplate transactionTemplate;

    @Scheduled(fixedRate = 3600000)
    public void updateAllPriorities(){
        transactionTemplate.execute(status -> {
            List<Task> tasks = taskRepository.findAllByTaskStatusNot(TaskStatus.COMPLETED);

            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);

                double newScore = taskPriorityService.calculatePriorityScore(task);
                task.setPriorityScore(newScore);

                if (LocalDateTime.now().isAfter(task.getDeadline())) {
                    notificationDispatcher.sendOverdueAlert(task.getUser(), task.getId(), task.getTitle());
                } else if (ChronoUnit.HOURS.between(LocalDateTime.now(), task.getDeadline()) <= 2) {
                    notificationDispatcher.sendCloseToOverdueAlert(task.getUser(), task.getId(), task.getTitle());
                }

                if (i % 1000 == 0 && i > 0) {
                    entityManager.flush();
                    entityManager.clear();
                }
            }
            entityManager.flush();
            entityManager.clear();
            return null;
        });
        log.info("Priorities updated! Done at: {}", LocalDateTime.now());
    }
}
