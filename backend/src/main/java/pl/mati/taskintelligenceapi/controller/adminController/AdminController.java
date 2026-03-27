package pl.mati.taskintelligenceapi.controller.adminController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mati.taskintelligenceapi.dto.RestResponse;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Admin Controller", description = "Endpoints for admin operations")
public class AdminController {
    @Operation(summary = "Test admin endpoint", description = "A simple endpoint to test if admin access is working.")
    @GetMapping("/test")
    public ResponseEntity<RestResponse<String>> testAdmin() {
        return ResponseEntity.ok(RestResponse.success("Admin endpoint is working!"));
    }
}
