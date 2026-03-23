package pl.mati.taskintelligenceapi.service.taskService;

import org.springframework.stereotype.Service;
import pl.mati.taskintelligenceapi.entity.Task;
import pl.mati.taskintelligenceapi.entity.enums.TaskStatus;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class TaskPriorityService {
    public double calculatePriority(Task task){
        if(task.getTaskStatus() == TaskStatus.COMPLETED) return task.getPriorityScore();

        long daysToDeadLine = ChronoUnit.HOURS.between(LocalDateTime.now(), task.getDeadlineTo())/24;

        long factor = Math.max(1, daysToDeadLine);

        double score = (task.getImportance() * 10.0) / factor;

        if(daysToDeadLine < 0){
            score += 50;
        }

        return score;
    }

    public int calculateScore(Task task) {
        double daysToDeadLine = ChronoUnit.HOURS.between(LocalDateTime.now(), task.getDeadlineTo())/24.0;
        return  (int) ((task.getImportance() * 10) + (daysToDeadLine * 5));
    }
}
