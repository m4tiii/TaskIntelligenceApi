package pl.mati.taskintelligenceapi.service.statisticsService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mati.taskintelligenceapi.dto.statisticsDto.StatisticsResponseDTO;
import pl.mati.taskintelligenceapi.dto.statisticsDto.StatsRequestDTO;
import pl.mati.taskintelligenceapi.entity.Statistics;
import pl.mati.taskintelligenceapi.entity.enums.TimeRange;
import pl.mati.taskintelligenceapi.entity.User;
import pl.mati.taskintelligenceapi.repository.StatisticRepository;
import pl.mati.taskintelligenceapi.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticRepository statisticRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<StatisticsResponseDTO> getStats(StatsRequestDTO dto, String username){

        User user =  userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));



        List<Statistics> stats = statisticRepository.findAllByUserAndCompletionDateBetween(user, dto.from(), dto.to());


        Map<String, Double> groupedStats = stats.stream()
                .collect(Collectors.groupingBy(
                        s -> formatByUnit(s.getCompletionDate(), dto.timeRange()),
                        TreeMap::new,
                        Collectors.summingDouble(Statistics::getScore)
                ));

        List<String> labels = generateLabels(dto.from(), dto.to(), dto.timeRange());

        return   labels.stream()
                .map(label -> new StatisticsResponseDTO(label, groupedStats.getOrDefault(label, 0.0)))
                .collect(Collectors.toList());
    }

    private List<String> generateLabels(LocalDate from, LocalDate to, TimeRange timeRange){
        List<String> labels = new ArrayList<>();
        LocalDate current = alignToPeriodStart(from, timeRange);

        while (!current.isAfter(to)) {
            labels.add(formatByUnit(current, timeRange));
            current = switch (timeRange) {
                case DAY -> current.plusDays(1);
                case WEEK -> current.plusWeeks(1);
                case MONTH -> current.plusMonths(1);
                case YEAR -> current.plusYears(1);
            };
        }
        return labels.stream().distinct().sorted().toList();
    }

    private String formatByUnit(LocalDate date, TimeRange unit){
        return switch (unit){
            case DAY -> date.toString();
            case WEEK -> {
                int daysToSubtract = date.getDayOfWeek().getValue() - 1;
                yield date.minusDays(daysToSubtract).toString();
            }
            case MONTH -> date.toString().substring(0, 7);
            case YEAR -> String.valueOf(date.getYear());
        };
    }

    private LocalDate alignToPeriodStart(LocalDate date, TimeRange timeRange){
        return switch (timeRange){
            case WEEK -> {
                int daysToSubtract = date.getDayOfWeek().getValue() - 1;
                yield date.minusDays(daysToSubtract);
            }
            case MONTH -> date.withDayOfMonth(1);
            case YEAR -> date.withDayOfYear(1);
            default -> date;
        };
    }
}
