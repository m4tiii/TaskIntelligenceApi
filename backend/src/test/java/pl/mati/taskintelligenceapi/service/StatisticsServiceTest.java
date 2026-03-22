package pl.mati.taskintelligenceapi.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.mati.taskintelligenceapi.dto.statisticsDto.StatisticsResponseDTO;
import pl.mati.taskintelligenceapi.dto.statisticsDto.StatsRequestDTO;
import pl.mati.taskintelligenceapi.entity.Statistics;
import pl.mati.taskintelligenceapi.entity.TimeRange;
import pl.mati.taskintelligenceapi.entity.User;
import pl.mati.taskintelligenceapi.repository.StatisticRepository;
import pl.mati.taskintelligenceapi.repository.UserRepository;
import pl.mati.taskintelligenceapi.service.statisticsService.StatisticsService;
import org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatisticsServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private StatisticRepository statisticRepository;
    @InjectMocks
    private StatisticsService statisticsService;

    @Test
    void shouldReturnGroupedStatsByMonth(){
        //GIVEN
        String username = "testUser";
        User user = new User();

        StatsRequestDTO dto = new StatsRequestDTO(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 1),
                TimeRange.MONTH
        );

        List<Statistics> mockStats = List.of(
                new Statistics(1L, 10.0, LocalDate.of(2024, 1, 10), user),
                new Statistics(2L, 20.0, LocalDate.of(2024, 1, 20), user),
                new Statistics(3L, 50.0, LocalDate.of(2024, 2, 15), user)
        );

        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(user));
        when(statisticRepository.findAllByUserAndCompletionDateBetween(user, dto.from(), dto.to())).thenReturn(mockStats);

        //WHEN

        List<StatisticsResponseDTO> result = statisticsService.getStats(dto, username);

        //THEN

        Assertions.assertEquals(3, result.size(), "Should be 2 groups january and february");
        Assertions.assertEquals("2024-01", result.get(0).label(), "First group should be january");
        Assertions.assertEquals(30.0, result.get(0).value(), "Total score should be 30");
        Assertions.assertEquals("2024-02", result.get(1).label(), "Second group should be february");
        Assertions.assertEquals(50.0, result.get(1).value(), "Total score should be 50");
        Assertions.assertEquals("2024-03", result.get(2).label(), "Third group should be march");
        Assertions.assertEquals(0.0, result.get(2).value(), "Total score should be 0");
    }

    @Test
    void shouldReturnGroupedStatsByWeek(){
        //GIVEN
        String username = "testUser";
        User user = new User();

        StatsRequestDTO dto = new StatsRequestDTO(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 1),
                TimeRange.WEEK
        );

        List<Statistics> mockStats = List.of(
                new Statistics(1L, 10.0, LocalDate.of(2024, 1, 10), user),
                new Statistics(2L, 20.0, LocalDate.of(2024, 1, 20), user),
                new Statistics(3L, 50.0, LocalDate.of(2024, 2, 15), user)
        );

        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(user));
        when(statisticRepository.findAllByUserAndCompletionDateBetween(user, dto.from(), dto.to())).thenReturn(mockStats);

        //WHEN

        List<StatisticsResponseDTO> result = statisticsService.getStats(dto, username);

        //THEN

        Assertions.assertEquals(9, result.size(), "Should be 12 groups");
        Assertions.assertEquals("2024-01-01", result.get(0).label(), "First group should be 1th january");
        Assertions.assertEquals(0.0, result.get(0).value(), "Total score should be 0");
        Assertions.assertEquals("2024-01-08", result.get(1).label(), "Second group should be 8th january");
        Assertions.assertEquals(10.0, result.get(1).value(), "Total score should be 10");
        Assertions.assertEquals("2024-01-15", result.get(2).label(), "Third group should be 15th january");
        Assertions.assertEquals(20.0, result.get(2).value(), "Total score should be 20");

    }

    @Test
    void shouldReturnGroupedStatsByDay(){
        //GIVEN
        String username = "testUser";
        User user = new User();

        StatsRequestDTO dto = new StatsRequestDTO(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 1),
                TimeRange.DAY
        );

        List<Statistics> mockStats = List.of(
                new Statistics(1L, 10.0, LocalDate.of(2024, 1, 10), user),
                new Statistics(2L, 20.0, LocalDate.of(2024, 1, 20), user),
                new Statistics(3L, 50.0, LocalDate.of(2024, 2, 15), user)
        );

        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(user));
        when(statisticRepository.findAllByUserAndCompletionDateBetween(user, dto.from(), dto.to())).thenReturn(mockStats);

        //WHEN

        List<StatisticsResponseDTO> result = statisticsService.getStats(dto, username);

        //THEN

        Assertions.assertEquals(61, result.size(), "Should be 60 groups");
        Assertions.assertEquals("2024-01-10", result.get(9).label(), "First group should be 10th january");
        Assertions.assertEquals(10.0, result.get(9).value(), "Total score should be 10");
        Assertions.assertEquals("2024-01-20", result.get(19).label(), "Second group should be 20th january");
        Assertions.assertEquals(20.0, result.get(19).value(), "Total score should be 20");
        Assertions.assertEquals("2024-02-15", result.get(45).label(), "Third group should be 15th february");
        Assertions.assertEquals(50.0, result.get(45).value(), "Total score should be 50");

    }

    @Test
    void shouldReturnGroupedStatsByYear(){
        //GIVEN
        String username = "testUser";
        User user = new User();

        StatsRequestDTO dto = new StatsRequestDTO(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 1),
                TimeRange.YEAR
        );

        List<Statistics> mockStats = List.of(
                new Statistics(1L, 10.0, LocalDate.of(2024, 1, 10), user),
                new Statistics(2L, 20.0, LocalDate.of(2024, 1, 20), user),
                new Statistics(3L, 50.0, LocalDate.of(2024, 2, 15), user)
        );

        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(user));
        when(statisticRepository.findAllByUserAndCompletionDateBetween(user, dto.from(), dto.to())).thenReturn(mockStats);

        //WHEN

        List<StatisticsResponseDTO> result = statisticsService.getStats(dto, username);

        //THEN

        Assertions.assertEquals(1, result.size(), "Should be 1 group");
        Assertions.assertEquals("2024", result.getFirst().label(), "First group should be 2024");
        Assertions.assertEquals(80.0, result.getFirst().value(), "Total score should be 80");

    }
}
