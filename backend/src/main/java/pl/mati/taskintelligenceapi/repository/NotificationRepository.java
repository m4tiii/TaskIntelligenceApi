package pl.mati.taskintelligenceapi.repository;


import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.mati.taskintelligenceapi.entity.Notification;
import pl.mati.taskintelligenceapi.entity.User;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @EntityGraph(attributePaths = {"user"})
    List<Notification> findByUserAndIsRead(User user, boolean isRead);

    Optional<Notification> findByIdAndUser(Long notificationId, User user);
}
