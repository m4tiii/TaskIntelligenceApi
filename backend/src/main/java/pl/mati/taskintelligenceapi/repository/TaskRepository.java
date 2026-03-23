package pl.mati.taskintelligenceapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.mati.taskintelligenceapi.entity.Task;
import pl.mati.taskintelligenceapi.entity.enums.TaskStatus;

import java.util.List;
import java.util.Optional;


public interface TaskRepository extends JpaRepository<Task, Long>
{
    List<Task> findAllByUserUsername(String username);
    Optional<Task> findByIdAndUserUsername(Long id, String username);

    List<Task> findAllByTaskStatusNot(TaskStatus taskStatus);

    Page<Task> findAllByUserUsername(Pageable pageable, String name);

    @Query("SELECT t FROM Task t WHERE t.user.username = ?1 AND t.priorityScore > ?2 ORDER BY t.priorityScore DESC")
    List<Task> findAllByUserUsernameAndPriorityScoreGreaterThanSorted(String name, int score);
}
