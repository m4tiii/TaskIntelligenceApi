package pl.mati.taskintelligenceapi.controller.notificationController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mati.taskintelligenceapi.dto.NotificationDTO;
import pl.mati.taskintelligenceapi.dto.RestResponse;
import pl.mati.taskintelligenceapi.service.notificationService.NotificationService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Controller", description = "Endpoints for managing user notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(summary = "Get unread notifications for the authenticated user", description = "Returns a list of unread notifications for the currently authenticated user.")
    @GetMapping("/lastNotifications")
    public ResponseEntity<RestResponse<List<NotificationDTO>>> getNotifications(Principal principal){
        return ResponseEntity.ok(RestResponse.success(notificationService.getNotifications(principal)));
    }

    @Operation(summary = "Mark notification as read", description = "Marks a notification as read.")
    @PatchMapping("/markAsRead/{notificationId}")
    public ResponseEntity<RestResponse<Void>> markAsRead(@PathVariable Long notificationId, Principal principal) {
        notificationService.markAsRead(notificationId, principal);
        return ResponseEntity.ok(RestResponse.success(null));
    }
}
