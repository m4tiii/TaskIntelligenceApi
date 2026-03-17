package pl.mati.taskintelligenceapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mati.taskintelligenceapi.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
