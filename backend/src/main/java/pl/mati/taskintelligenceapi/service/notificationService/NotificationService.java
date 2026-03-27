package pl.mati.taskintelligenceapi.service.notificationService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mati.taskintelligenceapi.dto.NotificationDTO;
import pl.mati.taskintelligenceapi.entity.Notification;
import pl.mati.taskintelligenceapi.entity.User;
import pl.mati.taskintelligenceapi.repository.NotificationRepository;
import pl.mati.taskintelligenceapi.repository.UserRepository;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<NotificationDTO> getNotifications(Principal principal){
        String username = principal.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username + "!"));

        List<NotificationDTO> notificationDTOS = notificationRepository.findByUserAndIsRead(user, false)
                .stream()
                .map( notification ->  {
                    return new NotificationDTO(
                            notification.getId(),
                            notification.getType(),
                            notification.getMessage(),
                            notification.getTaskId(),
                            notification.getCreatedAt()
                    );

                }).toList();
        return notificationDTOS;
    }

    @Transactional
    public void markAsRead(Long notificationId, Principal principal){
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + principal.getName() + "!"));
        Notification notification = notificationRepository.findByIdAndUser(notificationId, user)
                        .orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + notificationId + "!"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
