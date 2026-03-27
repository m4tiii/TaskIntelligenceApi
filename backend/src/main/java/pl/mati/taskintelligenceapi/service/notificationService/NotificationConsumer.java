package pl.mati.taskintelligenceapi.service.notificationService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import pl.mati.taskintelligenceapi.config.RabbiMQConfig;
import pl.mati.taskintelligenceapi.dto.NotificationDTO;
import pl.mati.taskintelligenceapi.entity.Notification;
import pl.mati.taskintelligenceapi.entity.User;
import pl.mati.taskintelligenceapi.repository.NotificationRepository;
import pl.mati.taskintelligenceapi.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class NotificationConsumer {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SimpUserRegistry simpUserRegistry;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @RabbitListener(queues = RabbiMQConfig.NOTIFICATION_QUEUE)
    public void handle(NotificationDTO payload, @Header("targerUser") String username) {
        boolean isUserOnline = simpUserRegistry.getUser(username) != null;
        if (isUserOnline) {
            simpMessagingTemplate.convertAndSendToUser(
                    username,
                    "/queue/notifications",
                    payload
            );
        } else {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

            Notification newNotification = Notification.builder()
                    .user(user)
                    .message(payload.message())
                    .type(payload.type())
                    .taskId(payload.taskId())
                    .build();
            notificationRepository.save(newNotification);
        }
    }
}
