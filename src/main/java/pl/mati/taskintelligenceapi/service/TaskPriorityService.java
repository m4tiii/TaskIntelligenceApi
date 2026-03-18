package pl.mati.taskintelligenceapi.service;

import org.springframework.stereotype.Service;
import pl.mati.taskintelligenceapi.entity.Task;
import pl.mati.taskintelligenceapi.entity.TaskStatus;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class TaskPriorityService {
    public double calculatePriority(Task task){
        if(task.getTaskStatus() == TaskStatus.COMPLETED) return 0;

        long daysToDeadLine = ChronoUnit.DAYS.between(LocalDate.now(), task.getDeadlineTo());

        long factor = Math.max(1, daysToDeadLine);

        double score = (task.getImportance() * 10.0) / factor;

        if(daysToDeadLine < 0){
            score += 50;
        }

        return score;
    }
}
