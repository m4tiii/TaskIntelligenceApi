package pl.mati.taskintelligenceapi.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.mati.taskintelligenceapi.dto.taskDto.TaskRequestDTO;
import pl.mati.taskintelligenceapi.dto.taskDto.TaskResponseDTO;
import pl.mati.taskintelligenceapi.entity.Task;
import pl.mati.taskintelligenceapi.entity.TaskStatus;
import pl.mati.taskintelligenceapi.entity.User;
import pl.mati.taskintelligenceapi.mapper.TaskMapper;
import pl.mati.taskintelligenceapi.repository.TaskRepository;
import pl.mati.taskintelligenceapi.repository.UserRepository;
import pl.mati.taskintelligenceapi.service.taskService.SmartTaskService;
import pl.mati.taskintelligenceapi.service.taskService.TaskPriorityService;
import pl.mati.taskintelligenceapi.service.taskService.TaskService;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    TaskRepository taskRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    TaskPriorityService taskPriorityService;
    @Spy
    TaskMapper taskMapper = new TaskMapper();
    @InjectMocks
    TaskService taskService;
    @InjectMocks
    SmartTaskService smartTaskService;

    @Test
    void shouldReturnTaskWhenFound(){
        // Given
        User user = new User();
        user.setUsername("userTest");
        Task task = new Task();
        task.setId(999L);
        task.setTitle("titleTest");
        task.setUser(user);

        Mockito.when(taskRepository.findByIdAndUserUsername(task.getId(), user.getUsername())).thenReturn(Optional.of(task));
        
        // When
        TaskResponseDTO taskResponseDTO = taskService.getTaskById(task.getId(), user.getUsername());
        
        // Then
        Assertions.assertNotNull(taskResponseDTO);
        Assertions.assertEquals("titleTest", taskResponseDTO.title());
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFound(){
        // Given
        String username = "userTest";
        Long randomId = 200L;

        Mockito.when(taskRepository.findByIdAndUserUsername(randomId, username)).thenReturn(Optional.empty());

        // When & Then
        Assertions.assertThrows(EntityNotFoundException.class, () -> taskService.getTaskById(randomId, username));
    }

    @Test
    void shouldUpdateTaskWhenFound(){
       //Given
        String username = "userTest";
        Long taskId = 200L;

        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("titleTest");
        existingTask.setDescription("descriptionTest");

        TaskRequestDTO updateRequest = new TaskRequestDTO("updatedTitle", "updatedDescription", LocalDateTime.of(2026, Month.MARCH, 31, 12,15), 5, TaskStatus.NEW);
        Mockito.when(taskRepository.findByIdAndUserUsername(taskId, username)).thenReturn(Optional.of(existingTask));

        //When

        TaskResponseDTO updatedTaskDTO = taskService.updateTask(taskId,updateRequest,username);

        //Then
        Assertions.assertEquals("updatedTitle", updatedTaskDTO.title());
        Assertions.assertEquals("updatedDescription", updatedTaskDTO.description());
    }

    @Test
    void shouldReturnListOfTasks(){
        //Given
        String username = "userTest";
        Long taskIdFirst = 123L;
        Long taskIdSecond = 124L;

        Task task1 = new Task();
        task1.setId(taskIdFirst);
        task1.setTitle("titleTest1");
        Task task2 = new Task();
        task2.setId(taskIdSecond);
        task2.setTitle("titleTest2");

        Mockito.when(taskRepository.findAllByUserUsername(username)).thenReturn(List.of(task1, task2));

        //When
        List<TaskResponseDTO> tasks = taskService.getAllTasksByUserUsername(username);

        //Then
        Assertions.assertEquals(2, tasks.size());
        Assertions.assertEquals("titleTest1", tasks.get(0).title());
        Assertions.assertEquals("titleTest2", tasks.get(1).title());
    }

    @Test
    void shouldReturnAPageOfTasks(){
        //Given
        String username = "userTest";
        Long taskIdFirst = 123L;
        Long taskIdSecond = 124L;
        Long taskIdThird = 125L;

        Pageable pageable = PageRequest.of(0,1);

        Task task1 = new Task();
        task1.setId(taskIdFirst);
        task1.setTitle("titleTest1");
        Task task2 = new Task();
        task2.setId(taskIdSecond);
        task2.setTitle("titleTest2");
        Task task3 = new Task();
        task3.setId(taskIdThird);
        task3.setTitle("titleTest3");

        Page<Task> taskPage = new PageImpl<>(List.of(task1,task2, task3));

        Mockito.when(taskRepository.findAllByUserUsername(pageable, username)).thenReturn(taskPage);

        //when

        Page<TaskResponseDTO> response = taskService.getPageOfTasks(pageable, username);

        //Then

        Assertions.assertNotNull(response);
        Assertions.assertEquals(3, response.getTotalElements());
        Mockito.verify(taskRepository).findAllByUserUsername(pageable, username);

    }

    @Test
    void shouldReturnListOfTasksSortedByPriority(){
        //Given
        String username = "userTest";
        Long taskIdFirst = 123L;
        Long taskIdSecond = 124L;

        Pageable pageable = PageRequest.of(0,2);

        Task task1 = new Task();
        task1.setId(taskIdFirst);
        task1.setTitle("titleTest1");
        task1.setImportance(10);
        task1.setCreatedAt(LocalDateTime.now());
        task1.setDeadlineTo(LocalDateTime.of(2026, Month.MARCH, 31, 12,15));
        Task task2 = new Task();
        task2.setId(taskIdSecond);
        task2.setTitle("titleTest2");
        task2.setCreatedAt(LocalDateTime.now());
        task2.setImportance(5);
        task2.setDeadlineTo(LocalDateTime.of(2026, Month.MARCH, 31, 12,15));

        Mockito.when(taskRepository.findAllByUserUsername(username)).thenReturn(List.of(task1, task2));
        Mockito.when(taskPriorityService.calculatePriority(task1)).thenReturn(100.0);
        Mockito.when(taskPriorityService.calculatePriority(task2)).thenReturn(50.0);

        //When

        Page<TaskResponseDTO> tasks = smartTaskService.getSmartTaskList(username, pageable);

        //Then
        Assertions.assertEquals(2, tasks.getTotalElements());
        Assertions.assertTrue(tasks.getContent().get(0).taskPriority() > tasks.getContent().get(1).taskPriority());

    }

    @Test
    void shouldReturnEmptyListWhenNoTasksFound(){
        //Given
        String username = "userTest";

        Mockito.when(taskRepository.findAllByUserUsername(username)).thenReturn(List.of());

        //When

        List<TaskResponseDTO> tasks = taskService.getAllTasksByUserUsername(username);

        //Then
        Assertions.assertNotNull(tasks);
        Assertions.assertTrue(tasks.isEmpty());
    }

    @Test
    void shouldDeleteTaskSuccessfully(){
        //Given
        String username = "userTest";
        Long taskId = 200L;

        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("titleTest");

        Mockito.when(taskRepository.findByIdAndUserUsername(taskId, username)).thenReturn(Optional.of(existingTask));

        //When

        taskService.deleteTask(taskId, username);

        //Then
        Mockito.verify(taskRepository, Mockito.times(1)).delete(existingTask);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingTask(){
        //Given
        String username = "userTest";
        Long taskId = 200L;

        TaskRequestDTO updateRequest = new TaskRequestDTO("someTitle", "someDescription", LocalDateTime.of(2026, Month.MARCH, 31, 12,15), 10, TaskStatus.NEW);

        Mockito.when(taskRepository.findByIdAndUserUsername(taskId, username)).thenReturn(Optional.empty());

        //When Then
        Assertions.assertThrows(EntityNotFoundException.class, () -> taskService.updateTask(taskId, updateRequest, username));
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingTask() {
        //Given
        String username = "userTest";
        Long taskId = 200L;

        Mockito.when(taskRepository.findByIdAndUserUsername(taskId, username)).thenReturn(Optional.empty());

        //When Then
        Assertions.assertThrows(EntityNotFoundException.class, () -> taskService.deleteTask(taskId, username));
        Mockito.verify(taskRepository, Mockito.never()).delete(Mockito.any(Task.class));
    }

    @Test
    void shouldCreateTaskSuccessfully(){
        //Given
        String username = "userTest";
        Long taskId = 200L;

        User testUser = new User();
        testUser.setUsername("userTest");
        testUser.setPassword("passTest");

        Task savedTask = new Task();
        savedTask.setId(taskId);
        savedTask.setTitle("testTitle");
        savedTask.setDescription("testDescription");
        savedTask.setUser(testUser);

        TaskRequestDTO taskRequestDTO = new TaskRequestDTO("testTitle", "testDescription", LocalDateTime.of(2026, Month.MARCH, 31, 12,15), 5, TaskStatus.NEW);
        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(savedTask);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUser));
        //When
        TaskResponseDTO createdTaskDTO = taskService.createTask(taskRequestDTO, username);
        //Then
        Assertions.assertNotNull(createdTaskDTO);
        Assertions.assertEquals(taskId, createdTaskDTO.id());
        Assertions.assertEquals("testTitle", createdTaskDTO.title());
        Assertions.assertEquals("testDescription", createdTaskDTO.description());
    }

    @Test
    void shouldThrowExceptionWhenCreatingTaskForNonExistingUser(){
        //Given
        String username = "userTest";
        Long taskId = 200L;

        Task savedTask = new Task();
        savedTask.setId(taskId);
        savedTask.setTitle("testTitle");
        savedTask.setDescription("testDescription");

        TaskRequestDTO taskRequestDTO = new TaskRequestDTO("testTitle", "testDescription", LocalDateTime.of(2026, Month.MARCH, 31, 12,15), 5, TaskStatus.NEW);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        //When then

        Assertions.assertThrows(EntityNotFoundException.class, () -> taskService.createTask(taskRequestDTO, username));
        Mockito.verify(taskRepository, Mockito.never()).save(Mockito.any(Task.class));

    }
}
