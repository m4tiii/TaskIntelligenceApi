package pl.mati.taskintelligenceapi.dataTest;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import pl.mati.taskintelligenceapi.entity.enums.Role;
import pl.mati.taskintelligenceapi.entity.Statistics;
import pl.mati.taskintelligenceapi.entity.User;
import pl.mati.taskintelligenceapi.repository.StatisticRepository;
import pl.mati.taskintelligenceapi.repository.UserRepository;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@ActiveProfiles("dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StatisticsRepositoryTest {

    @Autowired
    private StatisticRepository statisticRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldSaveUserAndStatistics() {
        User user = new User(
                null,
                "Jonn123",
                "john.doe@gmail.com",
                "John",
                "Doe",
                "Poland",
                "securePassword123",
                new ArrayList<>(),
                Role.ROLE_USER,
                "some-random-refresh-token",
                LocalDateTime.now().plusDays(7),
                new ArrayList<>()
        );

        userRepository.save(user);

        List<Statistics> stats = List.of(
                new Statistics(null, 10.0, LocalDate.of(2026, 3, 1), user),
                new Statistics(null, 20.0, LocalDate.of(2026, 3, 2), user),
                new Statistics(null, 15.0, LocalDate.of(2026, 3, 3), user)
        );

        statisticRepository.saveAll(stats);
        statisticRepository.flush();

        entityManager.clear();
        entityManager.flush();

        List<Statistics> results = statisticRepository.findAllByUserAndCompletionDateBetween(user, LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 10));

        assertThat(results).hasSize(3);
    }


}
