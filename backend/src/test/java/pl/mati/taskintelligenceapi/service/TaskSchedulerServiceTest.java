package pl.mati.taskintelligenceapi.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.mati.taskintelligenceapi.entity.Task;
import pl.mati.taskintelligenceapi.entity.enums.TaskStatus;
import pl.mati.taskintelligenceapi.repository.TaskRepository;
import pl.mati.taskintelligenceapi.service.taskService.TaskPriorityService;
import pl.mati.taskintelligenceapi.service.taskService.TaskSchedulerService;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TaskSchedulerServiceTest {
    @Mock
    TaskRepository taskRepository;
    @Mock
    TaskPriorityService taskPriorityService;
    @InjectMocks
    TaskSchedulerService taskSchedulerService;

    private Task buildTask(Long id, TaskStatus status) {
        Task task = new Task();
        task.setId(id);
        task.setTaskStatus(status);
        task.setImportance(5);
        task.setDeadline(LocalDateTime.now().plusDays(3));
        task.setPriorityScore(0.0);
        return task;
    }

    @Test
    void shouldUpdatePriorityScoreForAllNonCompletedTasks(){
        //Given
        Task task1 = buildTask(1L, TaskStatus.NEW);
        Task task2 = buildTask(2L, TaskStatus.IN_PROGRESS);

        Mockito.when(taskRepository.findAllByTaskStatusNot(TaskStatus.COMPLETED)).thenReturn(List.of(task1, task2));
        Mockito.when(taskPriorityService.calculatePriorityScore(task1)).thenReturn(7.5);
        Mockito.when(taskPriorityService.calculatePriorityScore(task2)).thenReturn(8.0);

        //When
        taskSchedulerService.updateAllPriorities();

        //Then
        Assertions.assertEquals(7.5, task1.getPriorityScore());
        Assertions.assertEquals(8.0, task2.getPriorityScore());
        Mockito.verify(taskRepository).saveAll(List.of(task1, task2));
        Mockito.verify(taskPriorityService).calculatePriorityScore(task1);
        Mockito.verify(taskPriorityService).calculatePriorityScore(task2);
    }

    @Test
    void shouldCallSaveAllEvenNoTasksFound(){
        //Given
        Mockito.when(taskRepository.findAllByTaskStatusNot(TaskStatus.COMPLETED)).thenReturn(List.of());

        //When
        taskSchedulerService.updateAllPriorities();

        //Then
        Mockito.verify(taskRepository).saveAll(List.of());
        Mockito.verifyNoInteractions(taskPriorityService);
    }

    @Test
    void shouldCallCalculatePriorityOncePerTask(){
        //Given
        Task task1 = buildTask(1L, TaskStatus.NEW);
        Task task2 = buildTask(2L, TaskStatus.IN_PROGRESS);

        Mockito.when(taskRepository.findAllByTaskStatusNot(TaskStatus.COMPLETED)).thenReturn(List.of(task1, task2));

        //When
        taskSchedulerService.updateAllPriorities();

        //Then
        Mockito.verify(taskPriorityService, Mockito.times(1)).calculatePriorityScore(task1);
        Mockito.verify(taskPriorityService, Mockito.times(1)).calculatePriorityScore(task2);
        Mockito.verifyNoMoreInteractions(taskPriorityService);
    }
}
