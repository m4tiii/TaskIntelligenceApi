package pl.mati.taskintelligenceapi.service.notificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pl.mati.taskintelligenceapi.config.RabbiMQConfig;
import pl.mati.taskintelligenceapi.dto.NotificationDTO;
import pl.mati.taskintelligenceapi.entity.User;


import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationDispatcher {
    private final RabbitTemplate rabbitTemplate;

    public void dispatch(User user, String message, Long taskId){

        NotificationDTO notificationPayload = new NotificationDTO(
                null,
                "OVERDUE_ALERT",
                message,
                taskId,
                OffsetDateTime.now()
        );

        rabbitTemplate.convertAndSend(
                RabbiMQConfig.NOTIFICATION_EXCHANGE,
                RabbiMQConfig.NOTIFICATION_ROUTING_KEY,
                notificationPayload,
                m -> {
                    m.getMessageProperties().setHeader("targerUser", user.getUsername());
                    return m;
                }
        );


    }

    public void sendOverdueAlert(User user, Long taskId, String taskTitle){
        dispatch(user, "Your task: `" + taskTitle + "' is after deadline!", taskId);
    }

    public void sendCloseToOverdueAlert(User user, Long taskId, String taskTitle){
        dispatch(user, "Your task: `" + taskTitle + "' is close to deadline!", taskId);
    }
}
