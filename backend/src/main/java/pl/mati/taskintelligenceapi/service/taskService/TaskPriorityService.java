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
    public double calculatePriorityScore(Task task){
        if(task.getTaskStatus() == TaskStatus.COMPLETED) return task.getPriorityScore();

        long halfDaysToDeadLine = (ChronoUnit.HOURS.between(LocalDateTime.now(), task.getDeadline()));
        halfDaysToDeadLine /= 12;
        log.info(String.valueOf(halfDaysToDeadLine));
        long factor = Math.max(1, halfDaysToDeadLine);
        log.info(String.valueOf(factor));
        double score = (task.getImportance() * 10.0) / factor;
        log.info(String.valueOf(score));
        if(halfDaysToDeadLine < 0){
            score += 50;
            log.info("+50");
        }

        return score;
    }

    public int calculateScoreOfCompletedTask(Task task) {
        double daysToDeadLine = ChronoUnit.HOURS.between(LocalDateTime.now(), task.getDeadline())/24.0;
        return  (int) ((task.getImportance() * 10) + (daysToDeadLine * 5));
    }
}
