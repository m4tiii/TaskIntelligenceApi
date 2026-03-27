package pl.mati.taskintelligenceapi.service.notificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mati.taskintelligenceapi.dto.NotificationDTO;
import pl.mati.taskintelligenceapi.entity.Notification;
import pl.mati.taskintelligenceapi.entity.User;
import pl.mati.taskintelligenceapi.repository.NotificationRepository;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationDispatcher {
    private final SimpMessagingTemplate messagingTemplate;
    private final SimpUserRegistry userRegistry;
    private final NotificationRepository notificationRepository;

    @Transactional
    public void sendOverdueAlert(User user, Long taskId, String taskTitle){
        String message = "Your task: `" + taskTitle + "' is after deadline!";

        boolean isUserOnline = userRegistry.getUser(user.getUsername()) != null;
        if(isUserOnline){
            log.info("Sending notification LIVE to user: {}", user.getUsername());

            NotificationDTO notificationPayload = new NotificationDTO(
                    null,
                    "OVERDUE_ALERT",
                    message,
                    taskId,
                    LocalDateTime.now()
            );

            messagingTemplate.convertAndSendToUser(
                    user.getUsername(),
                    "/queue/notifications",
                    notificationPayload
            );
        }else{
            log.info("User {} is offline. Saving notification to database", user.getUsername());

            Notification notification = Notification.builder()
                    .user(user)
                    .message(message)
                    .type("OVERDUE_ALERT")
                    .task_id(taskId)
                    .build();

            notificationRepository.save(notification);
        }
    }

    @Transactional
    public void sendCloseToOverdueAlert(User user, Long taskId, String taskTitle){
        String message = "Your task: `" + taskTitle + "' is close to deadline!";

        boolean isUserOnline = userRegistry.getUser(user.getUsername()) != null;
        if(isUserOnline) {
            log.info("Sending notification LIVE to user: {}", user.getUsername());

            NotificationDTO notificationPayload = new NotificationDTO(
                    null,
                    "CLOSE_TO_OVERDUE_ALERT",
                    message,
                    taskId,
                    LocalDateTime.now()
            );

            messagingTemplate.convertAndSendToUser(
                    user.getUsername(),
                    "/queue/notifications",
                    notificationPayload
            );
        }else{
            log.info("User {} is offline. Saving notification to database", user.getUsername());

            Notification notification = Notification.builder()
                    .user(user)
                    .message(message)
                    .type("CLOSE_TO_OVERDUE_ALERT")
                    .task_id(taskId)
                    .build();

            notificationRepository.save(notification);
        }
    }
}
