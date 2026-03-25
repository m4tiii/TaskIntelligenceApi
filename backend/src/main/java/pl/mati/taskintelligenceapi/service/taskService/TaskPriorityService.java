package pl.mati.taskintelligenceapi.service.taskService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.mati.taskintelligenceapi.entity.Task;
import pl.mati.taskintelligenceapi.entity.enums.TaskStatus;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class TaskPriorityService {
    public double calculatePriority(Task task){
        if(task.getTaskStatus() == TaskStatus.COMPLETED) return task.getPriorityScore();

        long daysToDeadLine = (ChronoUnit.HOURS.between(LocalDateTime.now(), task.getDeadline()));
        daysToDeadLine /= 12;
        log.info(String.valueOf(daysToDeadLine));
        long factor = Math.max(1, daysToDeadLine);
        log.info(String.valueOf(factor));
        double score = (task.getImportance() * 10.0) / factor;
        log.info(String.valueOf(score));
        if(daysToDeadLine < 0){
            score += 50;
            log.info("+50");
        }

        return score;
    }

    public int calculateScore(Task task) {
        double daysToDeadLine = ChronoUnit.HOURS.between(LocalDateTime.now(), task.getDeadline())/24.0;
        return  (int) ((task.getImportance() * 10) + (daysToDeadLine * 5));
    }
}
