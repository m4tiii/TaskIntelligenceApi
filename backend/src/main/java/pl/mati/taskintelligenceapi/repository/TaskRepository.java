package pl.mati.taskintelligenceapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.mati.taskintelligenceapi.entity.Task;
import pl.mati.taskintelligenceapi.entity.enums.TaskStatus;

import java.util.List;
import java.util.Optional;


public interface TaskRepository extends JpaRepository<Task, Long>
{
    @EntityGraph(attributePaths = {"user"})
    List<Task> findAllByUserUsername(String username);

    @EntityGraph(attributePaths = {"user"})
    Optional<Task> findByIdAndUserUsername(Long id, String username);

    @EntityGraph(attributePaths = {"user"})
    List<Task> findAllByTaskStatusNot(TaskStatus taskStatus);

    @EntityGraph(attributePaths = {"user"})
    Page<Task> findAllByUserUsername(Pageable pageable, String name);

    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT t FROM Task t WHERE t.user.username = ?1 ORDER BY t.priorityScore DESC LIMIT 10")
    List<Task> findAllByUserUsernameAndPriorityScoreGreaterThanSorted(String name, int score);
}
