package pl.mati.taskintelligenceapi.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.mati.taskintelligenceapi.entity.Statistics;
import pl.mati.taskintelligenceapi.entity.User;

import java.time.OffsetDateTime;
import java.util.List;

public interface StatisticRepository extends JpaRepository<Statistics, Long> {

    @EntityGraph(attributePaths = {"user"})
    List<Statistics> findAllByUserAndCompletionDateBetween(User user, OffsetDateTime from, OffsetDateTime to);
}
