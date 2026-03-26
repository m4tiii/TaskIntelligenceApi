package pl.mati.taskintelligenceapi.service.taskService;

import org.springframework.stereotype.Service;
import pl.mati.taskintelligenceapi.entity.Task;
import pl.mati.taskintelligenceapi.entity.enums.TaskStatus;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class TaskPriorityService {
    public double calculatePriorityScore(Task task){
        if(task.getTaskStatus() == TaskStatus.COMPLETED) return task.getPriorityScore();

        long halfDaysToDeadLine = (ChronoUnit.HOURS.between(LocalDateTime.now(), task.getDeadline()));
        halfDaysToDeadLine /= 12;
        long factor = Math.max(1, halfDaysToDeadLine);
        double score = (task.getImportance() * 10.0) / factor;
        if(halfDaysToDeadLine < 0){
            score += 50;
        }

        return score;
    }

    public int calculateScoreOfCompletedTask(Task task) {
        double daysToDeadLine = ChronoUnit.HOURS.between(LocalDateTime.now(), task.getDeadline())/24.0;
        return  (int) ((task.getImportance() * 10) + (daysToDeadLine * 5));
    }
}
