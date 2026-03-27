package pl.mati.taskintelligenceapi.dto;

import java.time.LocalDateTime;

public record NotificationDTO (
        Long id,
        String type,
        String message,
        Long taskId,
        LocalDateTime createdAt
){
}
