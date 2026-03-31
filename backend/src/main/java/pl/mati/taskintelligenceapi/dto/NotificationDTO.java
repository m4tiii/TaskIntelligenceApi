package pl.mati.taskintelligenceapi.dto;

import java.time.OffsetDateTime;

public record NotificationDTO (
        Long id,
        String type,
        String message,
        Long taskId,
        OffsetDateTime createdAt
){
}
