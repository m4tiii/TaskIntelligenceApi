package pl.mati.taskintelligenceapi.repository;

import org.springframework.data.repository.CrudRepository;
import pl.mati.taskintelligenceapi.entity.Task;

public interface TaskRepository extends CrudRepository<Task, Long>
{
}
