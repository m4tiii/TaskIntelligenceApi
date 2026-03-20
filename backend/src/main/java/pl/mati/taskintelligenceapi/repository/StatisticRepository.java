package pl.mati.taskintelligenceapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mati.taskintelligenceapi.entity.Statistics;

public interface StatisticRepository extends JpaRepository<Statistics, Long> {
}
