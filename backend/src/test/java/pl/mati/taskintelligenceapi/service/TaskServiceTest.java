package pl.mati.taskintelligenceapi.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.mati.taskintelligenceapi.dto.taskDto.StatusUpdateDto;
import pl.mati.taskintelligenceapi.dto.taskDto.TaskRequestDTO;
import pl.mati.taskintelligenceapi.dto.taskDto.TaskResponseDTO;
import pl.mati.taskintelligenceapi.entity.Task;
import pl.mati.taskintelligenceapi.entity.enums.TaskStatus;
import pl.mati.taskintelligenceapi.entity.User;
import pl.mati.taskintelligenceapi.mapper.TaskMapper;
import pl.mati.taskintelligenceapi.repository.StatisticRepository;
import pl.mati.taskintelligenceapi.repository.TaskRepository;
import pl.mati.taskintelligenceapi.repository.UserRepository;
import pl.mati.taskintelligenceapi.service.taskService.SmartTaskService;
import pl.mati.taskintelligenceapi.service.taskService.TaskPriorityService;
import pl.mati.taskintelligenceapi.service.taskService.TaskService;

import java.security.Principal;
import java.time.LocalDate;
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
    @Mock
    TaskMapper taskMapper;
    @Mock
    StatisticRepository statisticRepository;
    @InjectMocks
    TaskService taskService;
    @InjectMocks
    SmartTaskService smartTaskService;

    @BeforeEach
    void setUp(){
        taskService = new TaskService(taskRepository, userRepository, taskPriorityService, taskMapper, statisticRepository);
        smartTaskService = new SmartTaskService(taskRepository, taskMapper, taskPriorityService);
    }

    private Task buildTask(Long id, String title, double priorityScore, TaskStatus status){
        Task task = new Task();

        task.setId(id);
        task.setTitle(title);
        task.setPriorityScore(priorityScore);
        task.setTaskStatus(status);
        task.setImportance(5);
        task.setDeadline(LocalDateTime.now().plusDays(3));
        return task;
    }


    private TaskResponseDTO buildTaskResponseDTO(Long id, String title, double priorityScore, TaskStatus status){
        return new TaskResponseDTO(id, title, null, LocalDateTime.now(), LocalDateTime.now(), 5, status, priorityScore);
    }

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
        TaskResponseDTO expectedDto = new TaskResponseDTO(999L, "titleTest", null, null, null, 0, null, 0.0);
        Mockito.when(taskMapper.toDto(task)).thenReturn(expectedDto);

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

        TaskResponseDTO updatedDto = new TaskResponseDTO(taskId, "updatedTitle", "updatedDescription", null, null, 5, TaskStatus.NEW, 0.0);
        Mockito.when(taskMapper.toDto(existingTask)).thenReturn(updatedDto);

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

        TaskResponseDTO dto1 = new TaskResponseDTO(taskIdFirst, "titleTest1", null, null, null, 0, null, 0.0);
        TaskResponseDTO dto2 = new TaskResponseDTO(taskIdSecond, "titleTest2", null, null, null, 0, null, 0.0);
        Mockito.when(taskMapper.toDto(task1)).thenReturn(dto1);
        Mockito.when(taskMapper.toDto(task2)).thenReturn(dto2);

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

        Page<Task> taskPage = new PageImpl<>(List.of(task1, task2, task3));

        Mockito.when(taskRepository.findAllByUserUsername(pageable, username)).thenReturn(taskPage);
        Mockito.when(taskMapper.toDto(task1)).thenReturn(new TaskResponseDTO(taskIdFirst, "titleTest1", null, null, null, 0, null, 0.0));
        Mockito.when(taskMapper.toDto(task2)).thenReturn(new TaskResponseDTO(taskIdSecond, "titleTest2", null, null, null, 0, null, 0.0));
        Mockito.when(taskMapper.toDto(task3)).thenReturn(new TaskResponseDTO(taskIdThird, "titleTest3", null, null, null, 0, null, 0.0));

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
        task1.setDeadline(LocalDateTime.of(2026, Month.MARCH, 31, 12,15));
        Task task2 = new Task();
        task2.setId(taskIdSecond);
        task2.setTitle("titleTest2");
        task2.setCreatedAt(LocalDateTime.now());
        task2.setImportance(5);
        task2.setDeadline(LocalDateTime.of(2026, Month.MARCH, 31, 12,15));

        Page<Task> taskPage = new PageImpl<>(List.of(task1, task2), pageable, List.of(task1, task2).size());

        Mockito.when(taskRepository.findAllByUserUsername(pageable, username)).thenReturn(taskPage);
        Mockito.when(taskPriorityService.calculatePriority(task1)).thenReturn(100.0);
        Mockito.when(taskPriorityService.calculatePriority(task2)).thenReturn(50.0);

        TaskResponseDTO dto1 = new TaskResponseDTO(taskIdFirst, "titleTest1", null, null, null, 10, null, 100.0);
        TaskResponseDTO dto2 = new TaskResponseDTO(taskIdSecond, "titleTest2", null, null, null, 5, null, 50.0);
        Mockito.when(taskMapper.toDto(task1)).thenReturn(dto1);
        Mockito.when(taskMapper.toDto(task2)).thenReturn(dto2);

        //When

        Page<TaskResponseDTO> tasks = smartTaskService.getSmartTaskList(username, pageable);

        //Then
        Assertions.assertEquals(2, tasks.getTotalElements());
        Assertions.assertTrue(tasks.getContent().get(0).priorityScore() > tasks.getContent().get(1).priorityScore());

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

        Task mappedTask = new Task();
        mappedTask.setTitle("testTitle");
        mappedTask.setDescription("testDescription");
        Mockito.when(taskMapper.toTask(taskRequestDTO)).thenReturn(mappedTask);

        TaskResponseDTO createdDto = new TaskResponseDTO(taskId, "testTitle", "testDescription", null, null, 0, TaskStatus.NEW, 0.0);
        Mockito.when(taskMapper.toDto(savedTask)).thenReturn(createdDto);

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

    @Test
    void shouldReturnSuggestionsWhenTasksExceedThreshold(){
        //Given
       String username = "testUser";
       Principal principal = Mockito.mock(Principal.class);
       Mockito.when(principal.getName()).thenReturn(username);

       Task highPriorityTask = buildTask(1L, "High Priority Task", 55.0, TaskStatus.NEW);
       Task highPriorityTask2 = buildTask(2L, "High Priority Task", 35.0, TaskStatus.NEW);

       TaskResponseDTO highPriorityDto = buildTaskResponseDTO(1L, "High Priority Task", 55.0, TaskStatus.IN_PROGRESS);
       TaskResponseDTO highPriorityDto2 = buildTaskResponseDTO(2L, "High Priority Task2", 35.0, TaskStatus.NEW);

       Mockito.when(taskRepository.findAllByUserUsernameAndPriorityScoreGreaterThanSorted(username, 30)).thenReturn(List.of(highPriorityTask, highPriorityTask2));
       Mockito.when(taskMapper.toDto(highPriorityTask)).thenReturn(highPriorityDto);
       Mockito.when(taskMapper.toDto(highPriorityTask2)).thenReturn(highPriorityDto2);

       //When
        List<TaskResponseDTO> result = smartTaskService.getSuggestions(principal);

        //Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("High Priority Task", result.get(0).title());
        Assertions.assertEquals("High Priority Task2", result.get(1).title());
        Mockito.verify(taskRepository).findAllByUserUsernameAndPriorityScoreGreaterThanSorted(username, 30);
    }

    @Test
    void shouldReturnEmptyListWhenNoTasksExceedThreshold(){
        //Given
        String username = "testUser";
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn(username);

        Mockito.when(taskRepository.findAllByUserUsernameAndPriorityScoreGreaterThanSorted(username, 30)).thenReturn(List.of());

        //When

        List<TaskResponseDTO> result = smartTaskService.getSuggestions(principal);

        //Then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        Mockito.verify(taskRepository).findAllByUserUsernameAndPriorityScoreGreaterThanSorted(username, 30);
        Mockito.verifyNoInteractions(taskMapper);
    }

    @Test
    void shouldMapEachReturnedTaskToDto(){
        //Given
        String username = "userTest";
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn(username);

        Task task = buildTask(3L, "Mapped Task", 45.0, TaskStatus.NEW);
        TaskResponseDTO dto = buildTaskResponseDTO(3L, "MappedTask", 45.0, TaskStatus.NEW);
        Mockito.when(taskRepository.findAllByUserUsernameAndPriorityScoreGreaterThanSorted(username, 30)).thenReturn(List.of(task));
        Mockito.when(taskMapper.toDto(task)).thenReturn(dto);

        //Then
        List<TaskResponseDTO> result = smartTaskService.getSuggestions(principal);

        //Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Mockito.verify(taskMapper, Mockito.times(1)).toDto(task);
    }

    @Test
    void shouldPassCorrectUsernameToRepository(){
        //Given
        String username = "userTest";
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn(username);
        Mockito.when(taskRepository.findAllByUserUsernameAndPriorityScoreGreaterThanSorted(username, 30)).thenReturn(List.of());

        //Then
        List<TaskResponseDTO> result = smartTaskService.getSuggestions(principal);

        //Then
        Mockito.verify(taskRepository).findAllByUserUsernameAndPriorityScoreGreaterThanSorted(username, 30);
    }

    @Test
    void shouldReturnCalculatedPriorityScore(){
        //Given
        TaskPriorityService taskPriorityService1 = new TaskPriorityService();
        Task task = buildTask(4L, "Priority Task", 0.0, TaskStatus.NEW);

        //When
        int priorityScore = taskPriorityService1.calculateScore(task);

        //Then
        Assertions.assertEquals(65, priorityScore);

    }

    @Test
    void shouldReturnCalculatedPriority(){
        //Given
        TaskPriorityService taskPriorityService1 = new TaskPriorityService();
        Task task = buildTask(4L, "Priority Task", 0.0, TaskStatus.NEW);
        task.setDeadline(LocalDateTime.now().plusDays(3).plusMinutes(30));
        double calculatedPriority = 50/6.0;

        //When
        double priorityScore = taskPriorityService1.calculatePriority(task);

        //Then
        Assertions.assertEquals(calculatedPriority, priorityScore, 0.001);

    }

    @Test
    void shouldReturnZeroForCompletedTask(){
        //Given
        TaskPriorityService taskPriorityService1 = new TaskPriorityService();
        Task task = buildTask(4L, "Priority Task", 0.0, TaskStatus.COMPLETED);

        //When
        double result = taskPriorityService1.calculatePriority(task);

        //Then
        Assertions.assertEquals(0.0, result);
    }

    @Test
    void shouldAddPenaltyWhenDeadlineIsOverdue(){
        //Given
        TaskPriorityService taskPriorityService1 = new TaskPriorityService();
        Task task = buildTask(4L, "Priority Task", 0.0, TaskStatus.NEW);
        task.setDeadline(LocalDateTime.now().minusDays(1));
        double calculatedPriority = 50/1 + 50;

        //When
        double priorityScore = taskPriorityService1.calculatePriority(task);

        //Then
        Assertions.assertEquals(calculatedPriority, priorityScore, 0.001);
    }

    @Test
    void shouldUseFactorOneWhenDeadlineIsToday(){
        //Given
        TaskPriorityService taskPriorityService1 = new TaskPriorityService();
        Task task = buildTask(4L, "Priority Task", 0.0, TaskStatus.NEW);
        task.setDeadline(LocalDateTime.now());
        double calculatedPriority = 50.0;

        //When
        double priorityScore = taskPriorityService1.calculatePriority(task);

        //Then
        Assertions.assertEquals(calculatedPriority, priorityScore, 0.001);
    }

    @Test
    void shouldReturnHigherPriorityForHighImportance(){
        //Given
        TaskPriorityService taskPriorityService1 = new TaskPriorityService();
        Task highTask = buildTask(4L, "Priority Task", 0.0, TaskStatus.NEW);
        highTask.setImportance(10);
        Task lowTask = buildTask(4L, "Priority Task", 0.0, TaskStatus.NEW);
        lowTask.setImportance(1);

        //When
        double highTaskPrior = taskPriorityService1.calculatePriority(highTask);
        double lowTaskPrior = taskPriorityService1.calculatePriority(lowTask);

        //Then

        Assertions.assertTrue(highTaskPrior > lowTaskPrior);
    }

    @Test
    void shouldSetCompletedStatusAndSaveStatistics(){
        //Given
        Task task = buildTask(1L, "testUser", 0.0 , TaskStatus.IN_PROGRESS);
        User user = new User();
        user.setUsername("testUser");
        task.setUser(user);
        StatusUpdateDto statusUpdateDto = new StatusUpdateDto("COMPLETED");
        TaskResponseDTO taskResponseDTO = buildTaskResponseDTO(1L, "testUser", 0.0 , TaskStatus.COMPLETED);

        Mockito.when(taskRepository.findByIdAndUserUsername(task.getId(), "testUser")).thenReturn(Optional.of(task));
        Mockito.when(taskPriorityService.calculateScore(task)).thenReturn(100);
        Mockito.when(taskMapper.toDto(task)).thenReturn(taskResponseDTO);

        //Then
        TaskResponseDTO result = taskService.patchTaskStatus(task.getId(), statusUpdateDto, "testUser");

        //Then
        Assertions.assertEquals(TaskStatus.COMPLETED, result.taskStatus());
        Mockito.verify(taskRepository).save(task);
        Mockito.verify(statisticRepository).save(Mockito.argThat(stats ->
                    stats.getScore() == 100 &&
                    stats.getUser().getUsername().equals("testUser") &&
                    stats.getCompletionDate().equals(LocalDate.now())
                ));
        Assertions.assertNotNull(result);
    }

    @Test
    void shouldSetInProgressStatusAndNotSaveStatistics(){
        //Given
        Task task = buildTask(1L, "testUser", 0.0 , TaskStatus.IN_PROGRESS);
        StatusUpdateDto statusUpdateDto = new StatusUpdateDto("IN_PROGRESS");
        TaskResponseDTO taskResponseDTO = buildTaskResponseDTO(1L, "testUser", 0.0 , TaskStatus.IN_PROGRESS);

        Mockito.when(taskRepository.findByIdAndUserUsername(task.getId(), "testUser")).thenReturn(Optional.of(task));
        Mockito.when(taskMapper.toDto(task)).thenReturn(taskResponseDTO);
        //When

        TaskResponseDTO result = taskService.patchTaskStatus(task.getId(), statusUpdateDto, "testUser");

        //Then
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, result.taskStatus());
        Mockito.verify(taskRepository).save(task);
        Mockito.verifyNoInteractions(statisticRepository);
        Mockito.verifyNoInteractions(taskPriorityService);
        Assertions.assertNotNull(result);
    }

    @Test
    void shouldThrowWhenTaskNotFound(){
        //Given
        StatusUpdateDto dto = new StatusUpdateDto("COMPLETED");
        Mockito.when(taskRepository.findByIdAndUserUsername(999L, "testUser")).thenReturn(Optional.empty());

        //When & Then
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> taskService.patchTaskStatus(999L, dto, "testUser"));
        Mockito.verifyNoInteractions(statisticRepository);
        Mockito.verifyNoInteractions(taskPriorityService);
    }
}
