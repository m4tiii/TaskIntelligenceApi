package pl.mati.taskintelligenceapi.controller.notificationController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mati.taskintelligenceapi.dto.NotificationDTO;
import pl.mati.taskintelligenceapi.dto.RestResponse;
import pl.mati.taskintelligenceapi.service.notificationService.NotificationService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/lastNotifications")
    public ResponseEntity<RestResponse<List<NotificationDTO>>> getNotifications(Principal principal){
        return ResponseEntity.ok(RestResponse.success(notificationService.getNotifications(principal)));
    }

    @PatchMapping("/markAsRead/{notificationId}")
    public ResponseEntity<RestResponse<Void>> markAsRead(@PathVariable Long notificationId, Principal principal) {
        notificationService.markAsRead(notificationId, principal);
        return ResponseEntity.ok(RestResponse.success(null));
    }
}
