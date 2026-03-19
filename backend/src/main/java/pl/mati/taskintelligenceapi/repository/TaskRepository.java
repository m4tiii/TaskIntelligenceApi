package pl.mati.taskintelligenceapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.mati.taskintelligenceapi.entity.Task;
import pl.mati.taskintelligenceapi.entity.TaskStatus;

import java.util.List;
import java.util.Optional;


public interface TaskRepository extends JpaRepository<Task, Long>
{
    List<Task> findAllByUserUsername(String username);
    Optional<Task> findByIdAndUserUsername(Long id, String username);

    List<Task> findAllByTaskStatusNot(TaskStatus taskStatus);

    Page<Task> findAllByUserUsername(Pageable pageable, String name);
}
