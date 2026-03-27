package pl.mati.taskintelligenceapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationDispatcher {
    private final SimpMessagingTemplate messagingTemplate;

    public record NotificationPayload(String type, String message, Long taskId){}

    public void sendOverdueAlert(String username, Long taskId, String taskTitle){
        NotificationPayload payload = new NotificationPayload(
                "OVERDUE_ALERT",
                "Twoje zadanie: '"+ taskTitle +"' przekroczyło termin wykonania!",
                taskId
        );

        messagingTemplate.convertAndSendToUser(
                username,
                "/queue/notifications",
                payload
        );
    }
}
