package pl.mati.taskintelligenceapi.controller.adminController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mati.taskintelligenceapi.dto.RestResponse;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {
    @GetMapping("/test")
    public ResponseEntity<RestResponse<String>> testAdmin() {
        return ResponseEntity.ok(RestResponse.success("Admin endpoint is working!"));
    }
}
