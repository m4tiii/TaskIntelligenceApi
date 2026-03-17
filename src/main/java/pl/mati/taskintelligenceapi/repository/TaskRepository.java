package pl.mati.taskintelligenceapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mati.taskintelligenceapi.entity.Task;

import java.util.List;
import java.util.Optional;


public interface TaskRepository extends JpaRepository<Task, Long>
{
    List<Task> findAllByUserUsername(String username);
    Optional<Task> findByIdAndUserUsername(Long id, String username);
}
