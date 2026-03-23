package pl.mati.taskintelligenceapi.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.mati.taskintelligenceapi.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"user"})
    Optional<User> findByUsername(String username);
    @EntityGraph(attributePaths = {"user"})
    Optional<User> findByUsernameAndEmail(String username, String email);
}
