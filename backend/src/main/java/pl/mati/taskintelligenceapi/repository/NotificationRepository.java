package pl.mati.taskintelligenceapi.repository;


import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.mati.taskintelligenceapi.entity.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @EntityGraph(attributePaths = {"user"})
    List<Notification> findByUserUsernameAndIsRead(String username, boolean isRead);
}
