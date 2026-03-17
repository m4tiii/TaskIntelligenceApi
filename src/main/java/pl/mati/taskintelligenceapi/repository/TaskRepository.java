package pl.mati.taskintelligenceapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mati.taskintelligenceapi.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long>
{
}
