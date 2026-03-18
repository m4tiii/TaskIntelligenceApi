package pl.mati.taskintelligenceapi.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.mati.taskintelligenceapi.dto.TaskRequestDTO;
import pl.mati.taskintelligenceapi.dto.TaskResponseDTO;
import pl.mati.taskintelligenceapi.entity.Task;
import pl.mati.taskintelligenceapi.entity.User;
import pl.mati.taskintelligenceapi.repository.TaskRepository;
import pl.mati.taskintelligenceapi.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    TaskRepository taskRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    TaskService taskService;

    @Test
    void shouldReturnTaskWhenFound(){
        // Given (Przygotowanie danych i atrap)
        User user = new User();
        user.setUsername("userTest");
        Task task = new Task();
        task.setId(999L);
        task.setTitle("titleTest");
        task.setUser(user);

        Mockito.when(taskRepository.findByIdAndUserUsername(task.getId(), user.getUsername())).thenReturn(Optional.of(task));
        
        // When (Faktyczne wywołanie logiki)
        TaskResponseDTO taskResponseDTO = taskService.getTaskById(task.getId(), user.getUsername());
        
        // Then (Sprawdzenie wyników)
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

        TaskRequestDTO updateRequest = new TaskRequestDTO("updatedTitle", "updatedDescription");
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

        TaskRequestDTO updateRequest = new TaskRequestDTO("someTitle", "someDescription");

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

        TaskRequestDTO taskRequestDTO = new TaskRequestDTO("testTitle", "testDescription");
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

        TaskRequestDTO taskRequestDTO = new TaskRequestDTO("testTitle", "testDescription");

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        //When then

        Assertions.assertThrows(EntityNotFoundException.class, () -> taskService.createTask(taskRequestDTO, username));
        Mockito.verify(taskRepository, Mockito.never()).save(Mockito.any(Task.class));

    }
}
